package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.repositories.PreferencesRepository
import com.juraj.stocksbrowser.ui.home.screen.InstrumentType
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(symbol: String, type: InstrumentType) {
        when (type) {
            InstrumentType.Stock -> preferencesRepository.toggleFavoritesStocks(symbol)
            InstrumentType.ETF -> preferencesRepository.toggleFavoritesEtfs(symbol)
        }
    }
}
