package com.juraj.stocksbrowser.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.juraj.stocksbrowser.SearchTextField
import com.juraj.stocksbrowser.navigation.NavDestinations
import com.juraj.stocksbrowser.ui.theme.StockListHeaderItem
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme

data class HomeScreenState(
    val favorites: List<StockInfo> = emptyList(),
    val popularStock: List<StockInfo> = emptyList(),
    val popularEtfs: List<StockInfo> = emptyList()
)

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel, navController: NavController) {
    HomeScreen(viewModel.state.observeAsState().value ?: HomeScreenState()) {
        navController.navigate(NavDestinations.Details.uri(it.symbol))
    }
}

@Composable
private fun HomeScreen(state: HomeScreenState, openStockDetails: (StockInfo) -> Unit) {
    Scaffold(topBar = {
        TopAppBar {
            Text("Stocks Browser")
        }
    }) { paddingValue ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(paddingValue),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            SearchTextField { }

            LazyColumn {
                if (state.favorites.isNotEmpty())
                    addItemsWithHeader("Favorites", state.favorites, openStockDetails)

                if (state.popularStock.isNotEmpty())
                    addItemsWithHeader("Popular Stocks", state.popularStock, openStockDetails)

                if (state.popularEtfs.isNotEmpty())
                    addItemsWithHeader("Popular ETFs", state.popularEtfs, openStockDetails)
            }
        }
    }
}

private fun LazyListScope.addItemsWithHeader(
    text: String,
    items: List<StockInfo>,
    onClick: (StockInfo) -> Unit
) {
    item {
        StockListHeaderItem(text)
    }

    items.mapIndexed { index, stock ->
        item {
            StockListItem(stock, onClick)
            if (index != items.lastIndex) {
                Divider(modifier = Modifier.padding(horizontal = 24.dp))
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun HomeScreen_Preview() {
    StocksBrowserTheme {
        HomeScreen(
            HomeScreenState(
                favorites = listOf(
                    StockInfo(
                        symbol = "IBM",
                        name = "International Business Machines Corp",
                        exchange = "NYSE",
                        assetType = "Stock"
                    ),
                    StockInfo(
                        symbol = "TSLA",
                        name = "Tesla Inc",
                        exchange = "NYSE",
                        assetType = "Stock"
                    ),
                    StockInfo(
                        symbol = "CID",
                        name = "VICTORYSHARES INTERNATIONAL HIGH DIV VOLATILITY WTD ETF ",
                        exchange = "NASDAQ",
                        assetType = "ETF"
                    )
                )
            )
        ) {}
    }
}