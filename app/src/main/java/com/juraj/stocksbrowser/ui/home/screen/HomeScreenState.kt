package com.juraj.stocksbrowser.ui.home.screen

import com.juraj.stocksbrowser.data.StockEntity

data class HomeScreenState(
    val isLoading: Boolean = true,
    val isSearching: Boolean = false,
    val sections: Map<ScreenSection.Type, ScreenSection> = emptyMap()
)

data class ScreenSection(val isVisible: Boolean, val data: List<ListItem>) {
    enum class Type {
        MostPopularStocks,
        MostPopularEtfs,
        SearchResults,
        Favorites,
    }
}

sealed class HomeScreenSideEffect {
    data class NavigateToDetails(val symbol: String) : HomeScreenSideEffect()
}

enum class DeltaIndicator { Up, Down, NoChange }

sealed class ListItem {
    object ShimmerItem : ListItem()

    data class InstrumentItem(
        val symbol: String,
        val name: String,
        val lastSalePrice: String = "",
        val percentageChange: String = "",
        val deltaIndicator: DeltaIndicator
    ) : ListItem()

    data class HeaderItem(val type: HeaderType) : ListItem()
}


enum class HeaderType {
    Favorites, MostPopularStocks, MostPopularEtfs
}

fun StockEntity.toInstrumentItem() = ListItem.InstrumentItem(
    symbol = symbol,
    name = name,
    lastSalePrice = lastsale,
    percentageChange = pctchange,
    deltaIndicator = if (pctchange.startsWith("-")) DeltaIndicator.Down else DeltaIndicator.Up // TODO: change when data in DB are numeric
)