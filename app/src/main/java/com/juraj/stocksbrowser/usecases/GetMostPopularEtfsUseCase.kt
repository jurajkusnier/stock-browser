package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.model.room.EtfEntity
import com.juraj.stocksbrowser.repositories.EtfRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMostPopularEtfsUseCase @Inject constructor(
    private val repository: EtfRepository
) {

    operator fun invoke(): Flow<List<EtfEntity>> {
        return repository.getEtfs(symbols)
    }

    private val symbols = listOf("AAAU", "IVE", "IVW")
}