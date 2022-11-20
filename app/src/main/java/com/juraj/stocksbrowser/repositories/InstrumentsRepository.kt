package com.juraj.stocksbrowser.repositories

import com.juraj.stocksbrowser.data.InstrumentEntity
import com.juraj.stocksbrowser.data.InstrumentsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InstrumentsRepository @Inject constructor(private val instrumentsDao: InstrumentsDao) {

    suspend fun insertInstrument(instrumentEntity: InstrumentEntity) =
            instrumentsDao.insert(instrumentEntity)


    fun findInstruments(symbols: List<String>): Flow<List<InstrumentEntity>> =
        instrumentsDao.findSymbols(symbols)

}