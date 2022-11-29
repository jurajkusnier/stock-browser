package com.juraj.stocksbrowser.ui.detail.screen

import android.content.res.Configuration
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.himanshoe.charty.candle.model.CandleEntry
import com.juraj.stocksbrowser.ui.detail.components.CandleStickBox
import com.juraj.stocksbrowser.ui.detail.components.DetailsTable
import com.juraj.stocksbrowser.ui.detail.components.IntervalButtons
import com.juraj.stocksbrowser.ui.detail.components.PriceAndChange
import com.juraj.stocksbrowser.ui.detail.components.TopAppBarDetails
import com.juraj.stocksbrowser.ui.home.screen.DeltaIndicator
import com.juraj.stocksbrowser.ui.home.screen.InstrumentType
import com.juraj.stocksbrowser.ui.home.screen.ListItem
import com.juraj.stocksbrowser.ui.theme.StocksBrowserTheme
import com.juraj.stocksbrowser.usecases.GetRangeIntervalsUseCase
import com.juraj.stocksbrowser.usecases.toSelectable
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun DetailScreen(viewModel: DetailScreenViewModel, navController: NavController) {
    val viewState by viewModel.collectAsState()
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is DetailScreenSideEffect.NavigateHome -> navController.popBackStack()
        }
    }

    DetailScreen(viewState, viewModel::postIntent)
}

@Composable
private fun DetailScreen(state: DetailScreenState, action: (DetailScreenIntent) -> Unit) {
    val lazyListState = rememberLazyListState()
    val scrolledListState by remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 || lazyListState.firstVisibleItemScrollOffset > 0f } }
    Scaffold(topBar = {
        TopAppBarDetails(state.instrument, scrolledListState, state.isFavorite, {
            action(DetailScreenIntent.NavigateHome)
        }, {
            action(DetailScreenIntent.ToggleFav)
        })
    }) { paddingValues ->
        LazyColumn(state = lazyListState, contentPadding = paddingValues) {

            state.instrument?.let { instrument ->
                item {
                    PriceAndChange(
                        percentageChange = instrument.percentageChange,
                        lastSalePrice = instrument.lastSalePrice,
                        deltaIndicator = instrument.deltaIndicator
                    )
                }
            }

            item {
                CandleStickBox(candleStickData = state.candleStickData, yAxis = state.yAxis)
            }

            item {
                IntervalButtons(state.rangeIntervals) {
                    action(DetailScreenIntent.SelectRangeInterval(it))
                }
            }

            item {
                DetailsTable(state.details)
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Preview(showBackground = true, device = Devices.PIXEL_4, name = "Light Mode")
@Composable
fun DetailScreen_Preview() {
    StocksBrowserTheme {
        DetailScreen(
            DetailScreenState(
                instrument = ListItem.InstrumentItem(
                    symbol = "IBM",
                    name = "International Business Machines Corp",
                    lastSalePrice = "$15.8",
                    percentageChange = "0.5%",
                    deltaIndicator = DeltaIndicator.Up,
                    type = InstrumentType.Stock
                ),
                candleStickData = listOf(
                    CandleEntry(10f, 5f, 7f, 9f),
                    CandleEntry(11f, 7f, 9f, 8f),
                    CandleEntry(12f, 6f, 8f, 9f)
                ),
                yAxis = listOf("12", "6", "0"),
                rangeIntervals = GetRangeIntervalsUseCase().invoke()
                    .mapIndexed { index, rangeInterval -> rangeInterval.toSelectable(index == 1) },
                details = listOf(
                    Pair("Industry", "Data mining"),
                    Pair("Sector", "Internet and Telecommunication"),
                    Pair("IPO Year", "2019")
                )
            )
        ) {}
    }
}
