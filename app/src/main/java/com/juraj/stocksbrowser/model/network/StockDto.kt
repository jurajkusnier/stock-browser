package com.juraj.stocksbrowser.model.network

import kotlinx.serialization.Serializable

@Serializable
data class StockApiResponse(val data: StockData)

@Serializable
data class StockData(val rows: List<StockDto>)

@Serializable
data class StockDto(
    val symbol: String,
    val name: String,
    val lastsale: String,
    val pctchange: String,
    val volume: String,
    val marketCap: String,
    val country: String,
    val ipoyear: String,
    val industry: String,
    val sector: String
)
