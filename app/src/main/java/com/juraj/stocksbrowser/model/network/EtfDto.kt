package com.juraj.stocksbrowser.model.network

import kotlinx.serialization.Serializable

@Serializable
data class EtfApiResponse(val data: EtfData)

@Serializable
data class EtfData(val data: EtfDataData)

@Serializable
data class EtfDataData(val rows: List<EtfDto>)

@Serializable
data class EtfDto(
    val symbol: String,
    val companyName: String,
    val lastSalePrice: String,
    val netChange: String,
    val percentageChange: String
)