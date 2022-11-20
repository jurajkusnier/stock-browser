package com.juraj.stocksbrowser.repositories

import com.juraj.stocksbrowser.data.InstrumentEntity
import com.juraj.stocksbrowser.data.InstrumentsDao
import com.juraj.stocksbrowser.utils.toAlphaNumericString
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InstrumentsRepository @Inject constructor(private val instrumentsDao: InstrumentsDao) {

    suspend fun insertInstrument(instrumentEntity: InstrumentEntity) =
        instrumentsDao.insert(instrumentEntity)


    fun findInstruments(symbols: List<String>): Flow<List<InstrumentEntity>> =
        instrumentsDao.findSymbols(symbols)

    suspend fun searchInstruments(text: String): List<InstrumentEntity> =
        instrumentsDao.findByName(text.toAlphaNumericString() + "%")

    fun getInstrument(symbol: String): Flow<InstrumentEntity?> =
        instrumentsDao.getInstrument(symbol)

}