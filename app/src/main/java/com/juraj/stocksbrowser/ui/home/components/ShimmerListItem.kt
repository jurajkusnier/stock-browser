package com.juraj.stocksbrowser.ui.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juraj.stocksbrowser.ui.common.shimmerBackground
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme

@Composable
fun ShimmerListItem() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .size(width = 32.dp, height = 12.dp)
                    .shimmerBackground(RoundedCornerShape(4.dp))
            )
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth(0.75f)
                    .shimmerBackground(RoundedCornerShape(4.dp))
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(width = 32.dp, height = 12.dp)
                .shimmerBackground(RoundedCornerShape(4.dp))
        )
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(width = 32.dp, height = 12.dp)
                .shimmerBackground(RoundedCornerShape(4.dp))
        )
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun ShimmerListItem_Preview() {
    StocksBrowserTheme {
        ShimmerListItem()
    }
}
