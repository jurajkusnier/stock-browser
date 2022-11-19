package com.juraj.stocksbrowser.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme


data class StockInfo(
    val symbol: String,
    val name: String,
    val exchange: String,
    val assetType: String
)

@Composable
fun StockListItem(stockInfo: StockInfo, onClick: (StockInfo) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onClick(stockInfo) }
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Text(
            stockInfo.symbol,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            stockInfo.name,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = Ellipsis
        )
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun StockListItem_Preview() {
    StocksBrowserTheme {
        StockListItem(
            StockInfo(
                symbol = "IBM",
                name = "International Business Machines Corp",
                exchange = "NYSE",
                assetType = "Stock"
            )
        ) {}
    }
}