package com.juraj.stocksbrowser.ui.home.screen

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.juraj.stocksbrowser.model.room.InstrumentEntity
import com.juraj.stocksbrowser.ui.theme.darkGreen
import com.juraj.stocksbrowser.ui.theme.darkRed
import com.juraj.stocksbrowser.utils.format
import com.juraj.stocksbrowser.utils.toDeltaIndicator

data class HomeState(
    val isLoading: Boolean = true,
    val isSearching: Boolean = false,
    val sections: Map<ScreenSection.Type, ScreenSection> = emptyMap()
) {
    fun setSearching(isSearching: Boolean): HomeState {
        val sections = sections.toMutableMap()
        sections.remove(ScreenSection.Type.SearchResults)
        sections.keys.forEach { key ->
            val isVisible = if (isSearching)
                key == ScreenSection.Type.SearchResults
            else
                key != ScreenSection.Type.SearchResults

            sections[key]?.let { oldSection ->
                sections[key] = oldSection.copy(isVisible = isVisible)
            }
        }
        return copy(isSearching = isSearching, sections = sections)
    }

    fun updateSection(
        type: ScreenSection.Type,
        data: List<ListItem>
    ): HomeState {
        val sections = sections.toMutableMap()
        val header = type.toHeader()
        if (data.isEmpty()) {
            sections.remove(type)
        } else {
            sections[type] = ScreenSection(
                isVisible = if (isSearching)
                    type == ScreenSection.Type.SearchResults
                else
                    type != ScreenSection.Type.SearchResults,
                data = if (header != null) listOf(header).plus(data) else data
            )
        }
        return copy(sections = sections)
    }
}

data class ScreenSection(val isVisible: Boolean, val data: List<ListItem>) {
    enum class Type(val order: Int) {
        SearchResults(0),
        Favorites(1),
        MostPopularStocks(2),
        MostPopularEtfs(3),
    }
}

fun ScreenSection.Type.toHeader(): ListItem? = when (this) {
    ScreenSection.Type.SearchResults -> null
    ScreenSection.Type.Favorites -> ListItem.HeaderItem(HeaderType.Favorites)
    ScreenSection.Type.MostPopularStocks -> ListItem.HeaderItem(HeaderType.MostPopularStocks)
    ScreenSection.Type.MostPopularEtfs -> ListItem.HeaderItem(HeaderType.MostPopularEtfs)
}

sealed class HomeScreenSideEffect {
    data class NavigateToDetails(val symbol: String, val type: InstrumentType) :
        HomeScreenSideEffect()

    object NetworkError : HomeScreenSideEffect()
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

fun List<InstrumentEntity>.toInstrumentItems(): List<ListItem> {
    return if (isEmpty()) {
        List(5) { ListItem.ShimmerItem }
    } else {
        map { it.toInstrumentItem() }
    }
}

fun InstrumentEntity.toInstrumentItem() = ListItem.InstrumentItem(
    type = getType(),
    symbol = symbol,
    name = companyName,
    lastSalePrice = "$" + lastSalePrice.format(2),
    percentageChange = percentageChange.format(2) + "%",
    deltaIndicator = percentageChange.toDeltaIndicator()
)
