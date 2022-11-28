package com.juraj.stocksbrowser.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juraj.stocksbrowser.data.network.EtfDto

@Entity(tableName = "etfs")
data class EtfEntity(
    @PrimaryKey val symbol: String,
    val companyName: String,
    val lastSalePrice: String,
    val netChange: String,
    val percentageChange: String
)

fun EtfDto.toEtfEntity() = EtfEntity(
    symbol = symbol,
    companyName = companyName,
    lastSalePrice = lastSalePrice,
    netChange = netChange,
    percentageChange = percentageChange
)