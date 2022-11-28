package com.juraj.stocksbrowser.ui.home.screen

import com.juraj.stocksbrowser.data.room.EtfEntity
import com.juraj.stocksbrowser.data.room.StockEntity

data class HomeScreenState(
    val isLoading: Boolean = true,
    val isSearching: Boolean = false,
    val sections: Map<ScreenSection.Type, ScreenSection> = emptyMap()
)

data class ScreenSection(val isVisible: Boolean, val data: List<ListItem>) {
    enum class Type(val order: Int) {
        SearchResults(0),
        Favorites(1),
        MostPopularStocks(2),
        MostPopularEtfs(3),
    }
}

sealed class HomeScreenSideEffect {
    data class NavigateToDetails(val symbol: String, val type: InstrumentType) :
        HomeScreenSideEffect()
}

enum class DeltaIndicator { Up, Down, NoChange }

sealed class ListItem {
    object ShimmerItem : ListItem()

    data class InstrumentItem(
        val symbol: String,
        val name: String,
        val lastSalePrice: String = "",
        val percentageChange: String = "",
        val deltaIndicator: DeltaIndicator,
        val type: InstrumentType
    ) : ListItem()

    data class HeaderItem(val type: HeaderType) : ListItem()
}

enum class InstrumentType {
    Stock, ETF
}

enum class HeaderType {
    Favorites, MostPopularStocks, MostPopularEtfs
}

fun StockEntity.toInstrumentItem() = ListItem.InstrumentItem(
    type = InstrumentType.Stock,
    symbol = symbol,
    name = name,
    lastSalePrice = lastsale,
    percentageChange = pctchange,
    deltaIndicator = if (pctchange.startsWith("-")) DeltaIndicator.Down else DeltaIndicator.Up // TODO: change when data in DB are numeric
)

fun EtfEntity.toInstrumentItem() = ListItem.InstrumentItem(
    type = InstrumentType.ETF,
    symbol = symbol,
    name = companyName,
    lastSalePrice = lastSalePrice,
    percentageChange = percentageChange,
    deltaIndicator = if (percentageChange.startsWith("-")) DeltaIndicator.Down else DeltaIndicator.Up // TODO: change when data in DB are numeric
)