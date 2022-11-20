package com.juraj.stocksbrowser.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.LinearProgressIndicator
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

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel, navController: NavController) {
    viewModel.viewState.observeAsState().value?.let { viewState ->
        HomeScreen(viewState) {
            navController.navigate(NavDestinations.Details.uri(it.symbol))
        }
    }
}

@Composable
private fun HomeScreen(
    state: HomeScreenState,
    openStockDetails: (ListItem.InstrumentItem) -> Unit
) {
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

            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            SearchTextField { }

            LazyColumn {
                state.list.forEachIndexed { index, listItem ->
                    addListItem(listItem, openStockDetails)
                }
            }
        }
    }
}

private fun LazyListScope.addListItem(
    listItem: ListItem,
    onClick: (ListItem.InstrumentItem) -> Unit
) {
    item {
        when (listItem) {
            is ListItem.HeaderItem -> StockListHeaderItem(listItem.readableType())
            is ListItem.InstrumentItem -> InstrumentListItem(listItem, onClick)
            ListItem.ShimmerItem -> TODO()
        }
    }
}

private fun ListItem.HeaderItem.readableType(): String {
    return when (type) {
        HeaderType.Favorites -> "Favorites"
        HeaderType.MostPopularStocks -> "Most Popular Stocks"
        HeaderType.MostPopularEtfs -> "Most Popular ETFs"
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun HomeScreen_Preview() {
    StocksBrowserTheme {
        HomeScreen(
            HomeScreenState(
                list = listOf(
                    ListItem.HeaderItem(HeaderType.Favorites),
                    ListItem.InstrumentItem(
                        symbol = "IBM",
                        name = "International Business Machines Corp",
                        exchange = "NYSE",
                        assetType = "Stock"
                    ),
                    ListItem.HeaderItem(HeaderType.MostPopularStocks),
                    ListItem.InstrumentItem(
                        symbol = "TSLA",
                        name = "Tesla Inc",
                        exchange = "NYSE",
                        assetType = "Stock"
                    ),
                    ListItem.InstrumentItem(
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