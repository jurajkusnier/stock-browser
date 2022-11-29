package com.juraj.stocksbrowser.ui.detail.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.himanshoe.charty.candle.config.CandleStickConfig
import com.himanshoe.charty.candle.model.CandleEntry
import java.lang.Float.max
import kotlin.math.min

// Based on CandleStickChart but improving many bugs
// TODO: use CandleStickChart from library when bugs are fixed

@Composable
fun BetterCandleStickChart(
    candleEntryData: List<CandleEntry>,
    modifier: Modifier = Modifier,
    candleStickConfig: CandleStickConfig
) {

    Canvas(modifier = modifier) {
        val xBounds = Pair(
            0f,
            max(candleStickConfig.totalCandles.toFloat(), candleEntryData.count().toFloat())
        )
        val yBounds = getBounds(candleEntryData)
        val scaleX = size.width.div(xBounds.second.minus(xBounds.first))
        val scaleY = size.height.div(yBounds.second.minus(yBounds.first))
        val yMove = yBounds.first.times(scaleY)
        val interval = (0.rangeTo(min(candleEntryData.size.minus(1), candleEntryData.count())))

        interval.forEach { value ->
            val xPoint = value.times(scaleX).plus(scaleX.div(2))
            val yPointH = (size.height.minus(candleEntryData[value].high.times(scaleY))).plus(yMove)
            val yPointL = (size.height.minus(candleEntryData[value].low.times(scaleY))).plus(yMove)
            val yPointO =
                (size.height.minus(candleEntryData[value].opening.times(scaleY))).plus(yMove)
            val yPointC =
                (size.height.minus(candleEntryData[value].closing.times(scaleY))).plus(yMove)
            val path1 = Path()
            val path2 = Path()
            val isPositive = candleEntryData[value].opening <= candleEntryData[value].closing
            val candleColor =
                if (isPositive) candleStickConfig.positiveColor else candleStickConfig.negativeColor
            val candleLineColor =
                if (isPositive) candleStickConfig.positiveCandleLineColor else candleStickConfig.negativeCandleLineColor

            path1.moveTo(xPoint, yPointH)
            path1.lineTo(xPoint, yPointL)
            drawPath(
                path = path1,
                color = candleLineColor,
                style = Stroke(candleStickConfig.highLowLineWidth)
            )

            path2.moveTo(xPoint, yPointO)
            path2.lineTo(xPoint, yPointC)
            drawPath(
                path = path2,
                color = candleColor,
                style = Stroke(scaleX.minus(scaleX.div(8)))
            )
        }
    }
}

private fun getBounds(candleEntryList: List<CandleEntry>): Pair<Float, Float> {
    var min = Float.MAX_VALUE
    var max = -Float.MAX_VALUE
    candleEntryList.forEach {
        min = min.coerceAtMost(it.low)
        max = max.coerceAtLeast(it.high)
    }
    return Pair(min, max)
}
