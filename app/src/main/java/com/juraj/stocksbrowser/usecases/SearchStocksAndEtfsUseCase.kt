package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.data.InstrumentEntity
import com.juraj.stocksbrowser.repositories.InstrumentsRepository
import javax.inject.Inject

class SearchStocksAndEtfsUseCase @Inject constructor(
    private val instrumentsRepository: InstrumentsRepository
) {

    suspend operator fun invoke(text:String): List<InstrumentEntity> {
        return instrumentsRepository.searchInstruments(text)
    }

}