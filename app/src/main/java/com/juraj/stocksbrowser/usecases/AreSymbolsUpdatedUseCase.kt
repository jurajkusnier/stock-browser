package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class AreSymbolsUpdatedUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(): Flow<Boolean> {
        return preferencesRepository.getSymbolsUpdateDate().map {
            it != null && it > LocalDate.now().minusDays(3)
        }
    }
}