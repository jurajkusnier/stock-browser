package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.data.StockEntity
import com.juraj.stocksbrowser.repositories.StocksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchStocksAndEtfsUseCase @Inject constructor(
    private val stocksRepository: StocksRepository
) {

    operator fun invoke(text:String): Flow<List<StockEntity>> {
        return stocksRepository.findStocks(text)
    }

}