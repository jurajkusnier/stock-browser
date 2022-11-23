package com.juraj.stocksbrowser.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.juraj.stocksbrowser.ui.home.SearchTextField
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme

@Composable
fun SimpleTopAppBar(elevation: Dp, content: @Composable () -> Unit) {
    Surface(
        elevation = elevation,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        content = content
    )
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun SimpleTopAppBar_Preview() {
    StocksBrowserTheme {
        SimpleTopAppBar(0.dp) {
            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        "Stocks Browser",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                    )
                }

                SearchTextField {}
            }
        }
    }
}