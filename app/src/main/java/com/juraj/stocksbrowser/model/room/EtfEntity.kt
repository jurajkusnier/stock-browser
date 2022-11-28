package com.juraj.stocksbrowser.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juraj.stocksbrowser.model.network.EtfDto
import com.juraj.stocksbrowser.ui.home.screen.InstrumentType
import com.juraj.stocksbrowser.utils.toNumericString

@Entity(tableName = "etfs")
data class EtfEntity(
    @PrimaryKey override val symbol: String,
    override val companyName: String,
    override val lastSalePrice: Double,
    override val percentageChange: Double,
) : InstrumentEntity {
    override fun getType() = InstrumentType.ETF
}

fun EtfDto.toEtfEntity() = EtfEntity(
    symbol = symbol,
    companyName = companyName,
    lastSalePrice = lastSalePrice.toNumericString().toDouble(),
    percentageChange = percentageChange.toNumericString().toDouble()
)
