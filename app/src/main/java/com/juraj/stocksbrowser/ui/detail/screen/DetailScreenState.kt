package com.juraj.stocksbrowser.ui.detail.screen

import com.himanshoe.charty.candle.model.CandleEntry
import com.juraj.stocksbrowser.ui.home.screen.ListItem
import com.juraj.stocksbrowser.usecases.SelectableRangeInterval

data class DetailScreenState(
    val isLoading: Boolean = true,
    val instrument: ListItem.InstrumentItem? = null,
    val candleStickData: List<CandleEntry> = emptyList(),
    val yAxis: List<String> = emptyList(),
    val rangeIntervals: List<SelectableRangeInterval> = emptyList(),
    val details: List<Pair<String, String>> = emptyList(),
    val isFavorite: Boolean = false
)
