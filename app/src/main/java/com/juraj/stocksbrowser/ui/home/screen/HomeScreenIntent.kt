package com.juraj.stocksbrowser.ui.home.screen

sealed class HomeScreenIntent {
    object Refresh : HomeScreenIntent()
    data class Search(val value: String) : HomeScreenIntent()
    data class SetSearchState(val value: Boolean) : HomeScreenIntent()
    data class OpenDetail(val item: ListItem.InstrumentItem) : HomeScreenIntent()
}
