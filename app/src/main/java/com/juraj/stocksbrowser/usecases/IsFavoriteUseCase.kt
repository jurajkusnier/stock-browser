package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.repositories.PreferencesRepository
import com.juraj.stocksbrowser.ui.home.screen.InstrumentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(symbol: String, type: InstrumentType): Flow<Boolean> {
        return when (type) {
            InstrumentType.Stock -> preferencesRepository.getFavoritesStocks()
            InstrumentType.ETF -> preferencesRepository.getFavoritesEtfs()
        }.map { it.contains(symbol) }
    }
}
