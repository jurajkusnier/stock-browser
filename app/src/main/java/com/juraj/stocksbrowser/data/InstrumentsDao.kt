package com.juraj.stocksbrowser.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InstrumentsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(instrumentEntity: InstrumentEntity)

    @Query("SELECT * FROM instruments WHERE name LIKE :name OR symbol LIKE :name")
    suspend fun findByName(name: String): List<InstrumentEntity>

    @Query("SELECT * FROM instruments WHERE symbol IN (:symbols)")
    fun findSymbols(symbols: List<String>): Flow<List<InstrumentEntity>>

    @Query("SELECT * FROM instruments WHERE symbol == :symbol")
    fun getInstrument(symbol: String): Flow<InstrumentEntity?>

}