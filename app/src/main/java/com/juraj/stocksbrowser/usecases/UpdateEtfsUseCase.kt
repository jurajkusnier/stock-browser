package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.api.NasdaqApiService
import com.juraj.stocksbrowser.model.room.toEtfEntity
import com.juraj.stocksbrowser.repositories.EtfRepository
import timber.log.Timber
import javax.inject.Inject

class UpdateEtfsUseCase @Inject constructor(
    private val nasdaqApiService: NasdaqApiService,
    private val repository: EtfRepository,
) {

    suspend operator fun invoke(): Boolean {
        try {
            val apiResponse = nasdaqApiService.getEtfs() ?: return false
            apiResponse.data.data.rows.forEach { etf ->
                repository.insertEtf(etf.toEtfEntity())
            }
        } catch (e: Exception) {
            Timber.e(e)
            return false
        }

        return true
    }
}