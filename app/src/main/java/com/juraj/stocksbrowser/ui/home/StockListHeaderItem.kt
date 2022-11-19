package com.juraj.stocksbrowser.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StockListHeaderItem(text:String) {
    Text(
        text,
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(horizontal = 16.dp),
        fontWeight = FontWeight.Bold
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