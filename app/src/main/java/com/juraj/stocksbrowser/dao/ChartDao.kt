package com.juraj.stocksbrowser.dao

@kotlinx.serialization.Serializable
data class ChartApiResult(val chart: ChartDto)

@kotlinx.serialization.Serializable
data class ChartDto(val result: List<ResultDto>)

@kotlinx.serialization.Serializable
data class ResultDto(val indicators: IndicatorsDto)

@kotlinx.serialization.Serializable
data class IndicatorsDto(val quote: List<QuoteDto>)

@kotlinx.serialization.Serializable
data class QuoteDto(
    val open: List<Double?>,
    val low: List<Double?>,
    val close: List<Double?>,
    val volume: List<Double?>,
    val high: List<Double?>
)