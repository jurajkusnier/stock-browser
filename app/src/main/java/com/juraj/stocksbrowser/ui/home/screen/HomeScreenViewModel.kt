package com.juraj.stocksbrowser.ui.home.screen

import androidx.lifecycle.*
import com.juraj.stocksbrowser.data.StockEntity
import com.juraj.stocksbrowser.usecases.*
import com.juraj.stocksbrowser.utils.toSafeString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
//    updateSymbolsUseCase: UpdateSymbolsUseCase,
    private val areStocksUpdatedUseCase: AreStocksUpdatedUseCase,
    private val updateStocksUseCase: UpdateStocksUseCase,
    private val getMostPopularStocksUseCase: GetMostPopularStocksUseCase,
    getMostPopularEtfsUseCase: GetMostPopularEtfsUseCase,
    private val searchStocksAndEtfsUseCase: SearchStocksAndEtfsUseCase,
//    private val areSymbolsUpdatedUseCase: AreSymbolsUpdatedUseCase
) : ContainerHost<HomeScreenState, HomeScreenSideEffect>, ViewModel() {

    private var searchJob: Job? = null

    override val container = container<HomeScreenState, HomeScreenSideEffect>(HomeScreenState())

    private fun checkUpdate() {
        viewModelScope.launch {
            if (areStocksUpdatedUseCase().not()) {
                Timber.d("Try to update stocks")
                val success = updateStocksUseCase()
                Timber.d("Stocks update finished, success = $success")
            } else {
                Timber.d("Stocks are already updated")
            }
            intent {
                reduce {
                    state.copy(isLoading = false)
                }
            }
        }
    }

    private fun getMostPopularStocks() {
        viewModelScope.launch {
            getMostPopularStocksUseCase().collect { mostPopularStocks ->
                intent {
                    reduce {
                        val sections = state.sections.toMutableMap()
                        sections[ScreenSection.Type.MostPopularStocks] = ScreenSection(
                            isVisible = state.isSearching.not(),
                            data = listOf(ListItem.HeaderItem(HeaderType.MostPopularStocks))
                                .plus(mostPopularStocks.map { it.toInstrumentItem() })
                        )

                        state.copy(sections = sections)
                    }
                }
            }
        }
    }

    private fun updateSearchResults(result: List<StockEntity>) {
        intent {
            reduce {
                val sections = state.sections.toMutableMap()
                sections[ScreenSection.Type.SearchResults] = ScreenSection(
                    isVisible = state.isSearching,
                    data = result.map { it.toInstrumentItem() }
                )

                state.copy(sections = sections)
            }
        }
    }

    init {
        checkUpdate()

        getMostPopularStocks()

        // TODO: getMostPopularETFs()
    }

    fun postIntent(intent: HomeScreenIntent) {
        Timber.d("postIntent($intent)")
        when (intent) {
            is HomeScreenIntent.Search -> doSearch(intent.value)
            is HomeScreenIntent.SetSearchState -> {
                setSearchState(intent.value)
                doSearch("")
            }
            is HomeScreenIntent.OpenDetail -> navigateToDetail(intent.symbol)
        }
    }

    private fun navigateToDetail(symbol: String) {
        intent {
            postSideEffect(HomeScreenSideEffect.NavigateToDetails(symbol))
        }
    }

    private fun doSearch(value: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (value.toSafeString().isBlank()) {
                updateSearchResults(emptyList())
            } else {
                searchStocksAndEtfsUseCase(value).collect { searchResult ->
                    updateSearchResults(searchResult)
                }
            }
        }
    }

    private fun setSearchState(value: Boolean) {
        intent {
            reduce {
                val sections = state.sections.toMutableMap()
                sections.keys.forEach { key ->
                    val isVisible = if (value)
                        key == ScreenSection.Type.SearchResults
                    else
                        key != ScreenSection.Type.SearchResults

                    sections[key]?.let { oldSection ->
                        sections[key] = oldSection.copy(isVisible = isVisible)
                    }
                }

                state.copy(isSearching = value, sections = sections)
            }
        }
    }

}