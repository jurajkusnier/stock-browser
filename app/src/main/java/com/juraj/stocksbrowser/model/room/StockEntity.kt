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
}

fun StockDto.toStockEntity() = StockEntity(
    symbol = symbol,
    companyName = name.replace("Common Stock", "").trim(),
    lastSalePrice = lastsale.toNumericString().toDouble(),
    percentageChange = pctchange.toNumericString().toDouble(),
    volume = volume.toLongOrNull(),
    marketCap = marketCap.toLongOrNull(),
    country = country,
    ipoYear = ipoyear.toIntOrNull(),
    industry = industry,
    sector = sector
)
