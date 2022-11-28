package com.juraj.stocksbrowser.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juraj.stocksbrowser.dao.EtfDao
import com.juraj.stocksbrowser.dao.InstrumentsDao
import com.juraj.stocksbrowser.dao.StocksDao
import com.juraj.stocksbrowser.data.room.EtfEntity
import com.juraj.stocksbrowser.data.room.StockEntity

@Database(entities = [InstrumentEntity::class, StockEntity::class, EtfEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun instrumentsDao(): InstrumentsDao
    abstract fun stocksDao(): StocksDao
    abstract fun etfDao(): EtfDao
}