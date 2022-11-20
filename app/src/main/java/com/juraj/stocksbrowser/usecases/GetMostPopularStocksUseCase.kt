package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.data.InstrumentEntity
import com.juraj.stocksbrowser.repositories.InstrumentsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMostPopularStocksUseCase @Inject constructor(
    private val instrumentsRepository: InstrumentsRepository
) {

    operator fun invoke(): Flow<List<InstrumentEntity>> {
        return instrumentsRepository.findInstruments(favoriteStocks)
    }

    private val favoriteStocks = listOf("IBM", "MSFT", "TSLA", "AAPL", "GOOG")
}