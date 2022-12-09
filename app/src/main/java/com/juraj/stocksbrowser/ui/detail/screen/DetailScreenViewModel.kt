package com.juraj.stocksbrowser.ui.detail.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshoe.charty.candle.model.CandleEntry
import com.juraj.stocksbrowser.navigation.NavDestinations
import com.juraj.stocksbrowser.repositories.ChartsRepository
import com.juraj.stocksbrowser.repositories.EtfRepository
import com.juraj.stocksbrowser.repositories.StocksRepository
import com.juraj.stocksbrowser.ui.home.screen.InstrumentType
import com.juraj.stocksbrowser.ui.home.screen.extractDetails
import com.juraj.stocksbrowser.ui.home.screen.toInstrumentItem
import com.juraj.stocksbrowser.usecases.GetRangeIntervalsUseCase
import com.juraj.stocksbrowser.usecases.IsFavoriteUseCase
import com.juraj.stocksbrowser.usecases.RangeInterval
import com.juraj.stocksbrowser.usecases.ToggleFavoriteUseCase
import com.juraj.stocksbrowser.usecases.toSelectable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private val stockRepository: StocksRepository,
    private val etfRepository: EtfRepository,
    private val chartsRepository: ChartsRepository,
    private val getRangeIntervalsUseCase: GetRangeIntervalsUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ContainerHost<DetailScreenState, DetailScreenSideEffect>, ViewModel() {

    override val container =
        container<DetailScreenState, DetailScreenSideEffect>(DetailScreenState())

    private val symbol: String = NavDestinations.Details.getSymbol(savedStateHandle)
        ?: throw Exception("DetailScreenViewModel without symbol")

    private val type: InstrumentType = NavDestinations.Details.getType(savedStateHandle)
        ?: throw Exception("DetailScreenViewModel without type")

    private var selectedRangeInterval: RangeInterval = getRangeIntervalsUseCase()[1]
    private var loadChartJob: Job? = null

    private fun loadInstrument() {
        viewModelScope.launch {
            when (type) {
                InstrumentType.Stock -> loadStockDetails()
                InstrumentType.ETF -> loadEtfDetails()
            }
        }
    }

    private suspend fun loadStockDetails() {
        stockRepository.getStock(symbol)
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

    private suspend fun loadEtfDetails() {
        etfRepository.getEtf(symbol)
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

    private fun loadRangeIntervals() {
        getRangeIntervalsUseCase().let {
            intent {
                reduce {
                    state.copy(
                        rangeIntervals = it.map { rangeInterval ->
                            rangeInterval.toSelectable(rangeInterval == selectedRangeInterval)
                        }
                    )
                }
            }
        }
    }

    private fun loadChart() {
        loadChartJob?.cancel()

        loadChartJob = viewModelScope.launch {

            intent {
                reduce {
                    state.copy(isLoading = true)
                }
            }

            val apiResponse = try {
                chartsRepository.getChart(
                    symbol = symbol,
                    range = selectedRangeInterval.range,
                    interval = selectedRangeInterval.interval
                )
            } catch (e: Exception) {
                intent {
                    postSideEffect(DetailScreenSideEffect.NetworkError)
                }
                null
            }

            intent {
                reduce {
                    val candleData =
                        apiResponse?.chart?.result?.firstOrNull()?.indicators?.quote?.firstOrNull()

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
                            }
                        )
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
        loadFavorite()
    }

    fun postIntent(intent: DetailScreenIntent) {
        when (intent) {
            DetailScreenIntent.NavigateHome -> navigateHome()
            DetailScreenIntent.ToggleFav -> toggleFavorite()
            DetailScreenIntent.Refresh -> loadChart()
            is DetailScreenIntent.SelectRangeInterval -> setRangeInterval(intent.rangeInterval)
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch {
            toggleFavoriteUseCase(symbol, type)
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

    private fun loadFavorite() {
        viewModelScope.launch {
            isFavoriteUseCase(symbol, type).collect { value ->
                intent {
                    reduce {
                        state.copy(isFavorite = value)
                    }
                }
            }
        }
    }
}
