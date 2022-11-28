package com.juraj.stocksbrowser.ui.detail.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshoe.charty.candle.model.CandleEntry
import com.juraj.stocksbrowser.data.extractDetails
import com.juraj.stocksbrowser.navigation.NavDestinations
import com.juraj.stocksbrowser.repositories.ChartsRepository
import com.juraj.stocksbrowser.repositories.StocksRepository
import com.juraj.stocksbrowser.ui.home.screen.toInstrumentItem
import com.juraj.stocksbrowser.usecases.GetRangeIntervalsUseCase
import com.juraj.stocksbrowser.usecases.RangeInterval
import com.juraj.stocksbrowser.usecases.toSelectable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.roundToInt

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: StocksRepository,
    private val chartsRepository: ChartsRepository,
    private val getRangeIntervalsUseCase: GetRangeIntervalsUseCase
) : ContainerHost<DetailScreenState, DetailScreenSideEffect>, ViewModel() {

    override val container =
        container<DetailScreenState, DetailScreenSideEffect>(DetailScreenState())

    private val symbol: String = NavDestinations.Details.getSymbol(savedStateHandle)
        ?: throw Exception("DetailScreenViewModel without symbol")

    private var selectedRangeInterval: RangeInterval = getRangeIntervalsUseCase().first()

    private fun loadInstrument() {
        viewModelScope.launch {
            repository.getStock(symbol)
                .collect { instrumentEntity ->
                    intent {
                        reduce {
                            state.copy(
                                instrument = instrumentEntity?.toInstrumentItem(),
                                details = instrumentEntity?.extractDetails() ?: emptyList()
                            )
                        }
                    }
                }
        }
    }

    private fun loadRangeIntervals() {
        getRangeIntervalsUseCase().let {
            intent {
                reduce {
                    state.copy(rangeIntervals = it.map { rangeInterval ->
                        rangeInterval.toSelectable(rangeInterval == selectedRangeInterval)
                    })
                }
            }
        }
    }

    private fun loadChart() {
        viewModelScope.launch {
            val apiResponse = chartsRepository.getChart(
                symbol = symbol,
                range = selectedRangeInterval.range,
                interval = selectedRangeInterval.interval
            )
            intent {
                reduce {
                    val candleData =
                        apiResponse.chart.result.firstOrNull()?.indicators?.quote?.firstOrNull()

                    if (candleData != null) {
                        val lastIndex = min(candleData.high.lastIndex, candleData.low.lastIndex)

                        val minValue = candleData.high.filterNotNull().max()
                        val maxValue = candleData.low.filterNotNull().min()

                        state.copy(
                            isLoading = false,
                            yAxis = listOf(
                                "\$${minValue.roundToInt()}",
                                "\$${((maxValue + minValue) / 2).roundToInt()}",
                                "\$${maxValue.roundToInt()}"
                            ),
                            candleStickData = (0..lastIndex).mapNotNull { i ->
                                val high = candleData.high[i] ?: return@mapNotNull null
                                val low = candleData.low[i] ?: return@mapNotNull null
                                val closing = candleData.close[i] ?: return@mapNotNull null
                                val opening = candleData.open[i] ?: return@mapNotNull null
                                CandleEntry(
                                    opening = opening.toFloat(),
                                    closing = closing.toFloat(),
                                    high = high.toFloat(),
                                    low = low.toFloat()
                                )
                            })
                    } else {
                        state.copy(isLoading = false)
                    }
                }
            }
        }
    }

    init {
        loadRangeIntervals()
        loadInstrument()
        loadChart()
    }

    fun postIntent(intent: DetailScreenIntent) {
        when (intent) {
            DetailScreenIntent.NavigateHome -> navigateHome()
            DetailScreenIntent.ToggleFav -> TODO()
            is DetailScreenIntent.SelectRangeInterval -> setRangeInterval(intent.rangeInterval)
        }
    }

    private fun navigateHome() {
        intent {
            postSideEffect(DetailScreenSideEffect.NavigateHome)
        }
    }

    private fun setRangeInterval(value: RangeInterval) {
        selectedRangeInterval = value
        loadRangeIntervals()
        loadChart()
    }

}