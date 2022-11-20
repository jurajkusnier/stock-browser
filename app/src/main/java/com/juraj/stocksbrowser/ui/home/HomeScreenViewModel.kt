package com.juraj.stocksbrowser.ui.home

import androidx.lifecycle.*
import com.juraj.stocksbrowser.data.InstrumentEntity
import com.juraj.stocksbrowser.usecases.*
import com.juraj.stocksbrowser.utils.toAlphaNumericString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    updateSymbolsUseCase: UpdateSymbolsUseCase,
    getMostPopularStocksUseCase: GetMostPopularStocksUseCase,
    getMostPopularEtfsUseCase: GetMostPopularEtfsUseCase,
    searchStocksAndEtfsUseCase: SearchStocksAndEtfsUseCase,
    private val areSymbolsUpdatedUseCase: AreSymbolsUpdatedUseCase
) : ViewModel() {

    private val symbolsUpdateInProgress = MutableStateFlow(false)
    private val textFieldValue = MutableStateFlow("")

    private val searchResult: Flow<List<InstrumentEntity>> = textFieldValue.map {
        if (it.toAlphaNumericString().isBlank()) {
            emptyList()
        } else {
            searchStocksAndEtfsUseCase(it)
        }
    }

    val viewState = combine(
        getMostPopularStocksUseCase(),
        getMostPopularEtfsUseCase(),
        symbolsUpdateInProgress,
        textFieldValue,
        searchResult,
    ) { mostPopularStocks, mostPopularEtfs, loading, textFieldValue, searchResult ->
        HomeScreenState(
            isLoading = loading,
            textFieldValue = textFieldValue,
            list = mutableListOf<ListItem>().apply {
                if (textFieldValue.isNotBlank()) {
                    addAll(searchResult.map { it.toInstrumentItem() })
                }

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
        textFieldValue.tryEmit("")
        viewModelScope.launch {
            if (!areSymbolsUpdatedUseCase().first()) {
                symbolsUpdateInProgress.tryEmit(true)
                updateSymbolsUseCase()
            }
            symbolsUpdateInProgress.tryEmit(false)
        }
    }

    fun setTextFieldValue(value: String) {
        textFieldValue.tryEmit(value.replace('\n',' '))
    }
}