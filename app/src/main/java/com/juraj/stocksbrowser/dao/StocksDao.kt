package com.juraj.stocksbrowser.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juraj.stocksbrowser.model.room.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StocksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stockEntity: StockEntity)

    @Query("SELECT * FROM stocks WHERE symbol IN (:symbols)")
    fun getStocksBySymbols(symbols: List<String>): Flow<List<StockEntity>>

    @Query("SELECT * FROM stocks WHERE symbol == :symbol")
    fun getStockBySymbol(symbol: String): Flow<StockEntity?>

    @Query("SELECT * FROM stocks WHERE companyName LIKE :searchQuery OR symbol LIKE :searchQuery")
    fun findStocks(searchQuery: String): Flow<List<StockEntity>>
}
