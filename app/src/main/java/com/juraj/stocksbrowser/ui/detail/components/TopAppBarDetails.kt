package com.juraj.stocksbrowser.ui.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juraj.stocksbrowser.ui.common.SimpleTopAppBar
import com.juraj.stocksbrowser.ui.home.screen.DeltaIndicator
import com.juraj.stocksbrowser.ui.home.screen.ListItem
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme

@Composable
fun TopAppBarDetails(
    instrument: ListItem.InstrumentItem?,
    isElevated: Boolean,
    onBackArrowClick: () -> Unit,
    onStarClick: () -> Unit
) {
    SimpleTopAppBar(isElevated = isElevated) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackArrowClick) {
                Icon(Icons.Default.ArrowBack, null)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                instrument?.symbol?.let { symbol ->
                    Text(
                        symbol,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                    )
                }

                instrument?.name?.let { name ->
                    Text(
                        name,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(onClick = onStarClick) {
                Icon(Icons.Outlined.Grade, null) // TODO: use start for favorited item
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun TopAppBarDetails_Preview() {
    StocksBrowserTheme {
        TopAppBarDetails(instrument = ListItem.InstrumentItem(
            symbol = "IBM",
            name = "International Business Machines Corp is very long name",
            lastSalePrice = "$15.8",
            percentageChange = "0.5%",
            deltaIndicator = DeltaIndicator.Up
        ), false, {}, {})
    }
}
