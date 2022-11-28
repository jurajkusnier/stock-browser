package com.juraj.stocksbrowser.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juraj.stocksbrowser.model.room.EtfEntity
import com.juraj.stocksbrowser.model.room.StockEntity

@Database(entities = [StockEntity::class, EtfEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stocksDao(): StocksDao
    abstract fun etfDao(): EtfDao
}
