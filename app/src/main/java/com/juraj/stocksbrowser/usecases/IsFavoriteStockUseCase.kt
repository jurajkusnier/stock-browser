package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IsFavoriteStockUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(symbol: String): Flow<Boolean> {
        return preferencesRepository.getFavoritesStocks().map { it?.contains(symbol) == true }
    }
}