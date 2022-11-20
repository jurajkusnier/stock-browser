package com.juraj.stocksbrowser.ui.home

import androidx.lifecycle.*
import com.juraj.stocksbrowser.usecases.AreSymbolsUpdatedUseCase
import com.juraj.stocksbrowser.usecases.GetMostPopularEtfsUseCase
import com.juraj.stocksbrowser.usecases.GetMostPopularStocksUseCase
import com.juraj.stocksbrowser.usecases.UpdateSymbolsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    updateSymbolsUseCase: UpdateSymbolsUseCase,
    getMostPopularStocksUseCase: GetMostPopularStocksUseCase,
    getMostPopularEtfsUseCase: GetMostPopularEtfsUseCase,
    private val areSymbolsUpdatedUseCase: AreSymbolsUpdatedUseCase
) : ViewModel() {

    private val symbolsUpdateInProgress = MutableStateFlow(false)

    val viewState = combine(
        getMostPopularStocksUseCase(),
        getMostPopularEtfsUseCase(),
        symbolsUpdateInProgress
    ) { mostPopularStocks, mostPopularEtfs, loading ->
        HomeScreenState(
            isLoading = loading,
            list = mutableListOf<ListItem>().apply {
                if (mostPopularStocks.isNotEmpty()) {
                    add(ListItem.HeaderItem(HeaderType.MostPopularStocks))
                    addAll(mostPopularStocks.map { it.toInstrumentItem() })
                }

                if (mostPopularEtfs.isNotEmpty()) {
                    add(ListItem.HeaderItem(HeaderType.MostPopularEtfs))
                    addAll(mostPopularEtfs.map { it.toInstrumentItem() })
                }
            }
        )
    }.asLiveData()

    init {
        viewModelScope.launch {
            if (!areSymbolsUpdatedUseCase().first()) {
                symbolsUpdateInProgress.tryEmit(true)
                updateSymbolsUseCase()
            }
            symbolsUpdateInProgress.tryEmit(false)
        }
    }
}