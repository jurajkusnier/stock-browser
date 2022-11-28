package com.juraj.stocksbrowser.usecases

import javax.inject.Inject

data class RangeInterval(
    val range: String,
    val interval: String,
    val name: String
)

fun RangeInterval.toSelectable(isSelected: Boolean) = SelectableRangeInterval(
    range = range,
    interval = interval,
    name = name,
    isSelected = isSelected
)

data class SelectableRangeInterval(
    val range: String,
    val interval: String,
    val name: String,
    val isSelected: Boolean = false
)

fun SelectableRangeInterval.toRangeInterval() = RangeInterval(
    range = range,
    interval = interval,
    name = name
)

class GetRangeIntervalsUseCase @Inject constructor() {

    operator fun invoke(): List<RangeInterval> {
        return listOf(
            RangeInterval(range = "1d", interval = "30m", name = "1D"),
            RangeInterval(range = "5d", interval = "90m", name = "5D"),
            RangeInterval(range = "1mo", interval = "1d", name = "1M"),
            RangeInterval(range = "6mo", interval = "1wk", name = "6M"),
            RangeInterval(range = "1y", interval = "1wk", name = "1Y"),
        )
    }
}
