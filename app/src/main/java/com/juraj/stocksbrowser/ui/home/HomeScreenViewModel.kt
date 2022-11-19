package com.juraj.stocksbrowser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeScreenViewModel : ViewModel() {

    private val _state = MutableLiveData<HomeScreenState>()
    val state: LiveData<HomeScreenState>
        get() = _state

    init {
        _state.value = HomeScreenState(
            favorites = listOf(
                StockInfo(
                    symbol = "IBM",
                    name = "International Business Machines Corp",
                    exchange = "NYSE",
                    assetType = "Stock"
                ),
                StockInfo(
                    symbol = "TSLA",
                    name = "Tesla Inc",
                    exchange = "NYSE",
                    assetType = "Stock"
                ),
                StockInfo(
                    symbol = "CID",
                    name = "VICTORYSHARES INTERNATIONAL HIGH DIV VOLATILITY WTD ETF ",
                    exchange = "NASDAQ",
                    assetType = "ETF"
                )
            )
        )
    }
}