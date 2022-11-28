package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.data.room.StockEntity
import com.juraj.stocksbrowser.repositories.StocksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMostPopularStocksUseCase @Inject constructor(
    private val repository: StocksRepository
) {

    operator fun invoke(): Flow<List<StockEntity>> {
        return repository.getStocks(symbols)
    }

    private val symbols = listOf("IBM", "MSFT", "TSLA", "AAPL", "GOOG")
}