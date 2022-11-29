package com.juraj.stocksbrowser.ui.home.screen

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.juraj.stocksbrowser.model.room.EtfEntity
import com.juraj.stocksbrowser.model.room.InstrumentEntity
import com.juraj.stocksbrowser.model.room.StockEntity
import com.juraj.stocksbrowser.ui.theme.darkGreen
import com.juraj.stocksbrowser.ui.theme.darkRed
import com.juraj.stocksbrowser.utils.format
import com.juraj.stocksbrowser.utils.toDeltaIndicator

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
        val lastSalePrice: String,
        val percentageChange: String,
        val deltaIndicator: DeltaIndicator,
        val type: InstrumentType
    ) : ListItem()

    data class HeaderItem(val type: HeaderType) : ListItem()
}

@Composable
fun DeltaIndicator.toColor(): Color {
    return when (this) {
        DeltaIndicator.Up -> darkGreen
        DeltaIndicator.Down -> darkRed
        DeltaIndicator.NoChange -> MaterialTheme.colors.onSurface
    }
}

enum class InstrumentType {
    Stock, ETF
}

enum class HeaderType {
    Favorites, MostPopularStocks, MostPopularEtfs
}

fun InstrumentEntity.toInstrumentItem() = ListItem.InstrumentItem(
    type = getType(),
    symbol = symbol,
    name = companyName,
    lastSalePrice = "$" + lastSalePrice.format(2),
    percentageChange = percentageChange.format(2) + "%",
    deltaIndicator = percentageChange.toDeltaIndicator()
)

fun StockEntity.extractDetails(): List<Pair<String, String>> {
    return mutableListOf<Pair<String, String>>().apply {
        if (industry.isNotBlank()) add(Pair("Industry", industry))
        if (sector.isNotBlank()) add(Pair("Sector", sector))
        if (country.isNotBlank()) add(Pair("Country", country))
        if (ipoYear != null) add(Pair("IPO Year", ipoYear.toString()))
    }
}

fun EtfEntity.extractDetails(): List<Pair<String, String>> {
    return mutableListOf<Pair<String, String>>().apply {
        add(Pair("Name", companyName))
    }
}
