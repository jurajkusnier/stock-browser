package com.juraj.stocksbrowser.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [InstrumentEntity::class, StockEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun instrumentsDao(): InstrumentsDao
    abstract fun stocksDao(): StocksDao
}