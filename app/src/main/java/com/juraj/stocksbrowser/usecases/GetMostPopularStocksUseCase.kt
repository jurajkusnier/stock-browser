package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.data.StockEntity
import com.juraj.stocksbrowser.repositories.StocksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMostPopularStocksUseCase @Inject constructor(
    private val stocksRepository: StocksRepository
) {

    operator fun invoke(): Flow<List<StockEntity>> {
        return stocksRepository.getStocks(favoriteStocks)
    }

    private val favoriteStocks = listOf("IBM", "MSFT", "TSLA", "AAPL", "GOOG")
}