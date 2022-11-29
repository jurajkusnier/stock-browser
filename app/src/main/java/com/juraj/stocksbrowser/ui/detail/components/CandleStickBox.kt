package com.juraj.stocksbrowser.ui.detail.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.candle.config.CandleStickConfig
import com.himanshoe.charty.candle.model.CandleEntry
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme
import com.juraj.stocksbrowser.ui.theme.darkGreen
import com.juraj.stocksbrowser.ui.theme.darkRed

@Composable
fun CandleStickBox(candleStickData: List<CandleEntry>, yAxis: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        if (candleStickData.isNotEmpty()) {
            BetterCandleStickChart(
                modifier = Modifier
                    .fillMaxSize(),
                candleEntryData = candleStickData,
                candleStickConfig = CandleStickConfig(
                    positiveColor = darkGreen,
                    negativeColor = darkRed,
                    textColor = MaterialTheme.colors.onSurface,
                    highLowLineWidth = 4f,
                    shouldAnimateCandle = true,
                    showPriceText = false,
                    totalCandles = 16
                )
            )
        }

        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
            yAxis.forEach {
                Text(
                    it,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Preview(showBackground = true, device = Devices.PIXEL_4, name = "Light Mode")
@Composable
private fun CandleStickBox_Preview() {
    StocksBrowserTheme {
        CandleStickBox(
            candleStickData = listOf(
                CandleEntry(10f, 5f, 7f, 9f),
                CandleEntry(11f, 7f, 9f, 8f),
                CandleEntry(12f, 6f, 11f, 9f),
                CandleEntry(15f, 5f, 9f, 12f),
                CandleEntry(14f, 7f, 10f, 8f),
                CandleEntry(18f, 6f, 9f, 16f)
            ),
            yAxis = listOf("18", "11", "4"),
        )
    }
}
