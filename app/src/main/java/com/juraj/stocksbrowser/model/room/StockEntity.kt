package com.juraj.stocksbrowser.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juraj.stocksbrowser.model.network.StockDto
import com.juraj.stocksbrowser.ui.home.screen.InstrumentType
import com.juraj.stocksbrowser.utils.toNumericString

@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey override val symbol: String,
    override val companyName: String,
    override val lastSalePrice: Double,
    override val percentageChange: Double,
    val volume: Long?,
    val marketCap: Long?,
    val country: String,
    val ipoYear: Int?,
    val industry: String,
    val sector: String
) : InstrumentEntity {
    override fun getType() = InstrumentType.Stock
    override fun getDetails(): Map<String, String> = mutableMapOf<String, String>().apply {
        if (industry.isNotBlank()) set("Industry", industry)
        if (sector.isNotBlank()) set("Sector", sector)
        if (country.isNotBlank()) set("Country", country)
        if (ipoYear != null) set("IPO Year", ipoYear.toString())
    }
}

fun StockDto.toStockEntity() = StockEntity(
    symbol = symbol,
    companyName = name.replace("Common Stock", "").trim(),
    lastSalePrice = lastsale.toNumericString().toDoubleOrNull() ?: 0.0,
    percentageChange = pctchange.toNumericString().toDoubleOrNull() ?: 0.0,
    volume = volume.toLongOrNull(),
    marketCap = marketCap.toLongOrNull(),
    country = country,
    ipoYear = ipoyear.toIntOrNull(),
    industry = industry,
    sector = sector
)
