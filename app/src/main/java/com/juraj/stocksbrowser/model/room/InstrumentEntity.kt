package com.juraj.stocksbrowser.model.room

import com.juraj.stocksbrowser.ui.home.screen.InstrumentType

interface InstrumentEntity {
    val symbol: String
    val companyName: String
    val lastSalePrice: Double
    val percentageChange: Double
    fun getType(): InstrumentType
}
