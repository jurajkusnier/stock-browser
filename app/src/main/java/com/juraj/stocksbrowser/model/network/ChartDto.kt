package com.juraj.stocksbrowser.model.network

import kotlinx.serialization.Serializable

@Serializable
data class ChartApiResult(val chart: ChartDto)

@Serializable
data class ChartDto(val result: List<ResultDto>)

@Serializable
data class ResultDto(val indicators: IndicatorsDto)

@Serializable
data class IndicatorsDto(val quote: List<QuoteDto>)

@Serializable
data class QuoteDto(
    val open: List<Double?>,
    val low: List<Double?>,
    val close: List<Double?>,
    val volume: List<Double?>,
    val high: List<Double?>
)