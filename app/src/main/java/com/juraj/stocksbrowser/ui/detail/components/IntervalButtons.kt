package com.juraj.stocksbrowser.ui.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme
import com.juraj.stocksbrowser.usecases.*

@Composable
fun IntervalButtons(
    choices: List<SelectableRangeInterval>,
    onClick: (RangeInterval) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        choices.map {
            if (it.isSelected) {
                Button(onClick = { onClick(it.toRangeInterval()) }) {
                    Text(it.name)
                }
            } else {
                TextButton(onClick = { onClick(it.toRangeInterval()) }) {
                    Text(it.name)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun IntervalButtons_Preview() {
    StocksBrowserTheme {
        IntervalButtons(
            GetRangeIntervalsUseCase().invoke()
                .mapIndexed { index, item -> item.toSelectable(index == 3) }
        ) {}
    }
}