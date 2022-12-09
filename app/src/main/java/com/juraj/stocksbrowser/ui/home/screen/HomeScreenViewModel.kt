package com.juraj.stocksbrowser.ui.home.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juraj.stocksbrowser.usecases.AreEtfUpdatedUseCase
import com.juraj.stocksbrowser.usecases.AreStocksUpdatedUseCase
import com.juraj.stocksbrowser.usecases.GetFavoriteItemsUseCase
import com.juraj.stocksbrowser.usecases.GetMostPopularEtfsUseCase
import com.juraj.stocksbrowser.usecases.GetMostPopularStocksUseCase
import com.juraj.stocksbrowser.usecases.SearchStocksAndEtfsUseCase
import com.juraj.stocksbrowser.usecases.SetEtfsUpdatedUseCase
import com.juraj.stocksbrowser.usecases.SetStocksUpdatedUseCase
import com.juraj.stocksbrowser.usecases.UpdateEtfsUseCase
import com.juraj.stocksbrowser.usecases.UpdateStocksUseCase
import com.juraj.stocksbrowser.utils.toSafeString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.set

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val areStocksUpdatedUseCase: AreStocksUpdatedUseCase,
    private val setStocksUpdatedUseCase: SetStocksUpdatedUseCase,
    private val areEtfsUpdatedUseCase: AreEtfUpdatedUseCase,
    private val setEtfsUpdatedUseCase: SetEtfsUpdatedUseCase,
    private val updateStocksUseCase: UpdateStocksUseCase,
    private val updateEtfsUseCase: UpdateEtfsUseCase,
    private val getMostPopularStocksUseCase: GetMostPopularStocksUseCase,
    private val getMostPopularEtfsUseCase: GetMostPopularEtfsUseCase,
    private val searchStocksAndEtfsUseCase: SearchStocksAndEtfsUseCase,
    private val getFavoriteItemsUseCase: GetFavoriteItemsUseCase,
) : ContainerHost<HomeScreenState, HomeScreenSideEffect>, ViewModel() {

    private var searchJob: Job? = null
    private var checkForUpdateJob: Job? = null

    override val container = container<HomeScreenState, HomeScreenSideEffect>(HomeScreenState())

    private fun checkForUpdate() {
        checkForUpdateJob?.cancel()
        checkForUpdateJob = viewModelScope.launch {
            setLoadingState(true)
            delay(2000)
            val updateStocksSuccess = updateStocks()
            val updateEtfsSuccess = updateEtfs()
            setLoadingState(false)
            if (updateStocksSuccess.not() or updateEtfsSuccess.not()) {
                propagateError()
            }
        }
    }

    private suspend fun propagateError() {
        intent {
            postSideEffect(HomeScreenSideEffect.NetworkError)
        }
    }

    private suspend fun setLoadingState(value: Boolean) {
        intent {
            reduce {
                state.copy(isLoading = value)
            }
        }
    }

    private suspend fun updateStocks(): Boolean {
        return if (areStocksUpdatedUseCase().not()) {
            Timber.d("Try to update stocks")
            val success = updateStocksUseCase()
            if (success) setStocksUpdatedUseCase()
            Timber.d("Stocks update finished, success = $success")
            success
        } else {
            Timber.d("Stocks are already updated")
            true
        }
    }

    private suspend fun updateEtfs(): Boolean {
        return if (areEtfsUpdatedUseCase().not()) {
            Timber.d("Try to update ETFs")
            val success = updateEtfsUseCase()
            if (success) setEtfsUpdatedUseCase()
            Timber.d("ETFs update finished, success = $success")
            success
        } else {
            Timber.d("ETFs are already updated")
            true
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
                                .plus(mostPopularStocks.toInstrumentItems())
                        )

                        state.copy(sections = sections)
                    }
                }
            }
        }
    }

    private fun getMostPopularEtfs() {
        viewModelScope.launch {
            getMostPopularEtfsUseCase().collect { mostPopularEtfs ->
                intent {
                    reduce {
                        val sections = state.sections.toMutableMap()
                        sections[ScreenSection.Type.MostPopularEtfs] = ScreenSection(
                            isVisible = state.isSearching.not(),
                            data = listOf(ListItem.HeaderItem(HeaderType.MostPopularEtfs))
                                .plus(mostPopularEtfs.toInstrumentItems())
                        )

                        state.copy(sections = sections)
                    }
                }
            }
        }
    }

    private fun updateSearchResults(result: List<ListItem.InstrumentItem>) {
        intent {
            reduce {
                val sections = state.sections.toMutableMap()
                sections[ScreenSection.Type.SearchResults] = ScreenSection(
                    isVisible = state.isSearching,
                    data = result
                )

                state.copy(sections = sections)
            }
        }
    }

    init {
        checkForUpdate()

        getMostPopularStocks()

        getMostPopularEtfs()

        getFavItems()
    }

    fun postIntent(intent: HomeScreenIntent) {
        Timber.d("postIntent($intent)")
        when (intent) {
            is HomeScreenIntent.Search -> doSearch(intent.value)
            is HomeScreenIntent.SetSearchState -> {
                setSearchState(intent.value)
                doSearch("")
            }
            is HomeScreenIntent.OpenDetail -> navigateToDetail(intent.item)
            HomeScreenIntent.Refresh -> checkForUpdate()
        }
    }

    private fun navigateToDetail(item: ListItem.InstrumentItem) {
        intent {
            postSideEffect(HomeScreenSideEffect.NavigateToDetails(item.symbol, item.type))
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

    private fun getFavItems() {
        viewModelScope.launch {
            getFavoriteItemsUseCase().collect { favItems ->
                intent {
                    reduce {
                        val sections = state.sections.toMutableMap()
                        if (favItems.isEmpty()) {
                            sections.remove(ScreenSection.Type.Favorites)
                        } else {
                            sections[ScreenSection.Type.Favorites] = ScreenSection(
                                isVisible = state.isSearching.not(),
                                data = listOf(ListItem.HeaderItem(HeaderType.Favorites))
                                    .plus(favItems)
                            )
                        }

                        state.copy(sections = sections)
                    }
                }
            }
        }
    }
}
