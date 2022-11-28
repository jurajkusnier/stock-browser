package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.repositories.PreferencesRepository
import javax.inject.Inject

class ToggleFavoriteStockUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(symbol: String) {
        preferencesRepository.toggleFavoritesStocks(symbol)
    }
}