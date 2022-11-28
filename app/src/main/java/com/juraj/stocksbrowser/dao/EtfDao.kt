package com.juraj.stocksbrowser.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juraj.stocksbrowser.data.room.EtfEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EtfDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: EtfEntity)

    @Query("SELECT * FROM etfs WHERE symbol IN (:symbols)")
    fun getEtfsBySymbols(symbols: List<String>): Flow<List<EtfEntity>>

    @Query("SELECT * FROM etfs WHERE symbol == :symbol")
    fun getEtfBySymbol(symbol: String): Flow<EtfEntity?>

    @Query("SELECT * FROM etfs WHERE companyName LIKE :searchQuery OR symbol LIKE :searchQuery")
    fun findEtfs(searchQuery: String): Flow<List<EtfEntity>>
}