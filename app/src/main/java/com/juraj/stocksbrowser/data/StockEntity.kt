package com.juraj.stocksbrowser.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juraj.stocksbrowser.dao.StockDto
import okhttp3.internal.toLongOrDefault

@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey val symbol: String,
    val name: String,
    val lastsale: String,
    val pctchange: String,
    val volume: Long,
    val marketCap: Long,
    val country: String,
    val ipoyear: Int,
    val industry: String,
    val sector: String
)

fun StockDto.toStockEntity() = StockEntity(
    symbol = symbol,
    name = name,
    lastsale = lastsale,
    pctchange = pctchange,
    volume = volume.toLongOrDefault(0),
    marketCap = marketCap.toLongOrDefault(0),
    country = country,
    ipoyear = ipoyear.toIntOrNull() ?: 0,
    industry = industry,
    sector = sector
)

fun StockEntity.extractDetails(): List<Pair<String, String>> {
    return mutableListOf<Pair<String, String>>().apply {
        add(Pair("Industry", industry))
        add(Pair("Sector", sector))
        add(Pair("Country", country))
        add(Pair("IPO Year", ipoyear.toString()))
    }
}