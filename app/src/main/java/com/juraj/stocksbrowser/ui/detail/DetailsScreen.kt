package com.juraj.stocksbrowser.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.juraj.stocksbrowser.ui.home.ListItem
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme

@Composable
fun DetailScreen(viewModel: DetailScreenViewModel, navController: NavController) {
    viewModel.viewState.observeAsState().value?.let { viewState ->
        DetailScreen(viewState, navigateHome = {
            navController.popBackStack()
        })
    }
}

@Composable
private fun DetailScreen(state: DetailScreenState, navigateHome: () -> Unit) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(state.instrument.symbol) }, navigationIcon = {
            IconButton(navigateHome) {
                Icon(Icons.Default.ArrowBack, null)
            }
        })
    }) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {

        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DetailScreen_Preview() {
    StocksBrowserTheme {
        DetailScreen(
            DetailScreenState(
                ListItem.InstrumentItem(
                    symbol = "IBM",
                    name = "International Business Machines Corp",
                    exchange = "NYSE",
                    assetType = "Stock"
                )
            )
        ) {}
    }
}
