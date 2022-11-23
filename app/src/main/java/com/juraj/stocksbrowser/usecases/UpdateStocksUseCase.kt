package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.api.NasdaqApiService
import com.juraj.stocksbrowser.data.toStockEntity
import com.juraj.stocksbrowser.repositories.PreferencesRepository
import com.juraj.stocksbrowser.repositories.StocksRepository
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

class UpdateStocksUseCase @Inject constructor(
    private val nasdaqApiService: NasdaqApiService,
    private val stocksRepository: StocksRepository,
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(): Boolean {
        try {
            val apiResponse = nasdaqApiService.getStocks() ?: return false
            apiResponse.data.rows.forEach { stockDto ->
                stocksRepository.insertStock(stockDto.toStockEntity())
            }

            preferencesRepository.setStocksUpdateDate(LocalDate.now())

        } catch (e: Exception) {
            Timber.e(e)
            return false
        }

        return true
    }
}