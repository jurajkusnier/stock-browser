package com.juraj.stocksbrowser.ui.detail.screen

import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.himanshoe.charty.candle.config.CandleStickConfig
import com.himanshoe.charty.candle.model.CandleEntry
import com.juraj.stocksbrowser.ui.detail.components.BetterCandleStickChart
import com.juraj.stocksbrowser.ui.detail.components.DetailsTable
import com.juraj.stocksbrowser.ui.detail.components.IntervalButtons
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
                    Text(
                        instrument.percentageChange,
                        Modifier
                            .padding(top = 24.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = when (instrument.deltaIndicator) {
                            DeltaIndicator.Up -> Color(0xFF53AD8A) // TODO: separate
                            DeltaIndicator.Down -> Color(0xFFDE536D) // TODO: separate
                            DeltaIndicator.NoChange -> MaterialTheme.colors.onSurface
                        }
                    )
                }
            }

            state.instrument?.let { instrument ->
                item {
                    Text(
                        instrument.lastSalePrice,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    if (state.candleStickData.isNotEmpty()) {
                        BetterCandleStickChart(
                            modifier = Modifier
                                .fillMaxSize(),
                            candleEntryData = state.candleStickData,
                            candleStickConfig = CandleStickConfig(
                                positiveColor = Color.Green,
                                negativeColor = Color.Red,
                                textColor = Color.Black,
                                highLowLineWidth = 4f,
                                shouldAnimateCandle = true,
                                showPriceText = false,
                                totalCandles = 16
                            )
                        )
                    }

                    Column(Modifier.fillMaxHeight(), verticalArrangement = SpaceBetween) {
                        state.yAxis.forEach {
                            Text(
                                it,
                                modifier = Modifier.padding(start = 16.dp),
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
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

@Preview(showBackground = true, device = Devices.PIXEL_4)
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
                    Pair("Sector", "Internet and Telecomunication"),
                    Pair("IPO Year", "2019")
                )
            )
        ) {}
    }
}
