package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.repositories.PreferencesRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
internal class AreStocksUpdatedUseCaseTest {

    private val repository = mockk<PreferencesRepository>()
    private val useCase = AreStocksUpdatedUseCase(repository)

    @Test
    fun `WHEN shocks have been update less than 1 day ago SHOULD return TRUE`() = runTest {
        coEvery { repository.getStocksUpdateDate() } returns LocalDate.now()
        useCase() shouldBe true
    }

    @Test
    fun `WHEN stocks have been update more than 1 day ago SHOULD return FALSE`() = runTest {
        coEvery { repository.getStocksUpdateDate() } returns LocalDate.now().minusDays(1)
        useCase() shouldBe false
    }
}
