package com.juraj.stocksbrowser.ui.home.screen

import androidx.lifecycle.viewModelScope
import com.juraj.stocksbrowser.ui.common.MviViewModel
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
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
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
) : MviViewModel<HomeState, HomeScreenSideEffect, HomeIntent>(HomeState()) {

    private var searchJob: Job? = null
    private var checkForUpdateJob: Job? = null

    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.Search -> doSearch(intent.value)
            is HomeIntent.SetSearchState -> {
                updateState { it.setSearching(intent.value) }
            }
            is HomeIntent.OpenDetail -> navigateToDetail(intent.item)
            HomeIntent.Refresh -> checkForUpdate()
        }
    }

    init {
        checkForUpdate()
        loadMostPopularStocks()
        loadMostPopularEtfs()
        loadFavItems()
    }

    private fun checkForUpdate() {
        checkForUpdateJob?.cancel()
        checkForUpdateJob = viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            val successfullyUpdated = updateStocks() && updateEtfs()
            updateState { it.copy(isLoading = false) }
            if (successfullyUpdated.not()) {
                propagateError()
            }
        }
    }

    private fun propagateError() {
        postSideEffect(HomeScreenSideEffect.NetworkError)
    }

    private suspend fun updateStocks(): Boolean {
        return if (areStocksUpdatedUseCase().not()) {
            Timber.d("Updating stocks")
            updateStocksUseCase().also { success ->
                if (success) setStocksUpdatedUseCase()
                Timber.d("Stocks update finished, success = $success")
            }
        } else {
            Timber.d("Stocks are already updated")
            true
        }
    }

    private suspend fun updateEtfs(): Boolean {
        return if (areEtfsUpdatedUseCase().not()) {
            Timber.d("Updating ETFs")
            updateEtfsUseCase().also { success ->
                if (success) setEtfsUpdatedUseCase()
                Timber.d("ETFs update finished, success = $success")
            }
        } else {
            Timber.d("ETFs are already updated")
            true
        }
    }

    private fun loadMostPopularStocks() {
        viewModelScope.launch {
            getMostPopularStocksUseCase().collect { mostPopularStocks ->
                updateState {
                    it.updateSection(
                        ScreenSection.Type.MostPopularStocks,
                        mostPopularStocks.toInstrumentItems()
                    )
                }
            }
        }
    }

    private fun loadMostPopularEtfs() {
        viewModelScope.launch {
            getMostPopularEtfsUseCase().collect { mostPopularEtfs ->
                updateState {
                    it.updateSection(
                        ScreenSection.Type.MostPopularEtfs,
                        mostPopularEtfs.toInstrumentItems()
                    )
                }
            }
        }
    }

    private fun loadFavItems() {
        viewModelScope.launch {
            getFavoriteItemsUseCase().collect { favItems ->
                updateState { it.updateSection(ScreenSection.Type.Favorites, favItems) }
            }
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
                updateState { it.updateSection(ScreenSection.Type.SearchResults, emptyList()) }
            } else {
                searchStocksAndEtfsUseCase(value).collect { searchResult ->
                    updateState { it.updateSection(ScreenSection.Type.SearchResults, searchResult) }
                }
            }
        }
    }
}
