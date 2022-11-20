package com.juraj.stocksbrowser.ui.home

import com.juraj.stocksbrowser.data.InstrumentEntity

data class HomeScreenState(
    val isLoading: Boolean = true,
    val list: List<ListItem> = emptyList()
)

sealed class ListItem {
    object ShimmerItem : ListItem()

    data class InstrumentItem(
        val symbol: String,
        val name: String,
        val exchange: String,
        val assetType: String
    ) : ListItem()

    data class HeaderItem(val type: HeaderType) : ListItem()
}

enum class HeaderType {
    Favorites, MostPopularStocks, MostPopularEtfs
}

fun InstrumentEntity.toInstrumentItem() = ListItem.InstrumentItem(
    symbol = symbol,
    name = name,
    exchange = exchange,
    assetType = assetType
)