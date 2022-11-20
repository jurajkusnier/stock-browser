package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.data.InstrumentEntity
import com.juraj.stocksbrowser.repositories.InstrumentsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMostPopularEtfsUseCase @Inject constructor(
    private val instrumentsRepository: InstrumentsRepository
) {

    operator fun invoke(): Flow<List<InstrumentEntity>> {
        return instrumentsRepository.findInstruments(favoriteEtfs)
    }

    private val favoriteEtfs = listOf("AAAU", "IVE", "IVW")
}