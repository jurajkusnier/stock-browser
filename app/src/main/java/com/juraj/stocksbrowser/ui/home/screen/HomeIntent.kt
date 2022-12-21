package com.juraj.stocksbrowser.ui.home.screen

sealed class HomeIntent {
    object Refresh : HomeIntent()
    data class Search(val value: String) : HomeIntent()
    data class SetSearchState(val value: Boolean) : HomeIntent()
    data class OpenDetail(val item: ListItem.InstrumentItem) : HomeIntent()
}
