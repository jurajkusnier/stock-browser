package com.juraj.stocksbrowser.ui.detail.screen

import com.himanshoe.charty.candle.model.CandleEntry
import com.juraj.stocksbrowser.model.room.InstrumentEntity
import com.juraj.stocksbrowser.ui.home.screen.ListItem
import com.juraj.stocksbrowser.ui.home.screen.toInstrumentItem
import com.juraj.stocksbrowser.usecases.SelectableRangeInterval

data class DetailState(
    val isLoading: Boolean = true,
    val instrument: ListItem.InstrumentItem? = null,
    val candleStickData: List<CandleEntry> = emptyList(),
    val yAxis: List<String> = emptyList(),
    val rangeIntervals: List<SelectableRangeInterval> = emptyList(),
    val details: Map<String, String> = emptyMap(),
    val isFavorite: Boolean = false
) {
    fun setInstrumentEntity(entity: InstrumentEntity): DetailState {
        return copy(
            instrument = entity.toInstrumentItem(),
            details = entity.getDetails()
        )
    }
}
