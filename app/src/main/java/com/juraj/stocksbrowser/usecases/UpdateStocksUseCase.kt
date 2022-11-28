package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.api.NasdaqApiService
import com.juraj.stocksbrowser.model.room.toStockEntity
import com.juraj.stocksbrowser.repositories.StocksRepository
import timber.log.Timber
import javax.inject.Inject

class UpdateStocksUseCase @Inject constructor(
    private val nasdaqApiService: NasdaqApiService,
    private val repository: StocksRepository
) {

    suspend operator fun invoke(): Boolean {
        try {
            val apiResponse = nasdaqApiService.getStocks() ?: return false
            apiResponse.data.rows.forEach { stockDto ->
                repository.insertStock(stockDto.toStockEntity())
            }
        } catch (e: Exception) {
            Timber.e(e)
            return false
        }

        return true
    }
}