package com.juraj.stocksbrowser.ui.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme

@Composable
fun DetailsTable(data: List<Pair<String, String>>) {
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            "Details",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        data.forEachIndexed { index, pair ->
            Row() {
                Text(
                    pair.first,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f).padding(vertical = 2.dp)
                )
                Text(
                    pair.second,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.weight(2f).padding(vertical = 2.dp)
                )
            }
            if (index != data.lastIndex) {
                Divider(
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                    thickness = 1.dp
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DetailScreen_Preview() {
    StocksBrowserTheme {
        DetailsTable(
            listOf(
                Pair("Industry", "Data mining"),
                Pair("Sector", "Internet and Telecomunication"),
                Pair("IPO Year", "2019")
            )
        )
    }
}
