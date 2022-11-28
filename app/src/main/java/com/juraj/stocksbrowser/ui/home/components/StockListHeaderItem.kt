package com.juraj.stocksbrowser.ui.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme

@Composable
fun StockListHeaderItem(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.h2,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun StockListItem_Preview() {
    StocksBrowserTheme {
        StockListHeaderItem(
            "Favorites"
        )
    }
}