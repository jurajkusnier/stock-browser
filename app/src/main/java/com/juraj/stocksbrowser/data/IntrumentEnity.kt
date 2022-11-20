package com.juraj.stocksbrowser.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "instruments")
data class InstrumentEntity(
    @PrimaryKey val symbol: String,
    val name: String,
    val exchange: String,
    val assetType: String,
    val isFavorited: Boolean = false
)