package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.data.StockEntity
import com.juraj.stocksbrowser.repositories.PreferencesRepository
import com.juraj.stocksbrowser.repositories.StocksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class GetFavoriteStocksUseCase @Inject constructor(
    private val stocksRepository: StocksRepository,
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(): Flow<List<StockEntity>> {
        return preferencesRepository.getFavoritesStocks().flatMapMerge {
            stocksRepository.getStocks(it?.toList() ?: emptyList())
        }
    }
}