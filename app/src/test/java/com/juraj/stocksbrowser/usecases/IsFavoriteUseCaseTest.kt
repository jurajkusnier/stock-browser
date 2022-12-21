package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.repositories.PreferencesRepository
import com.juraj.stocksbrowser.ui.home.screen.InstrumentType
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class IsFavoriteUseCaseTest {

    private val repository = mockk<PreferencesRepository>().apply {
        every { getFavoritesStocks() } returns flowOf(setOf(FAV_STOCK))
        every { getFavoritesEtfs() } returns flowOf(setOf(FAV_ETF))
    }
    private val useCase = IsFavoriteUseCase(repository)

    @Test
    fun `WHEN symbol is favorite stock SHOULD return TRUE`() = runTest {
        useCase(FAV_STOCK, InstrumentType.Stock).first() shouldBe true
    }

    @Test
    fun `WHEN symbol is favorite etf SHOULD return TRUE`() = runTest {
        useCase(FAV_ETF, InstrumentType.ETF).first() shouldBe true
    }

    @Test
    fun `WHEN symbol is not favorite SHOULD return FALSE`() = runTest {
        useCase(FAV_STOCK, InstrumentType.ETF).first() shouldBe false
        useCase(NOT_FAV, InstrumentType.ETF).first() shouldBe false
        useCase(NOT_FAV, InstrumentType.Stock).first() shouldBe false
        useCase(FAV_ETF, InstrumentType.Stock).first() shouldBe false
    }

    companion object {
        const val FAV_STOCK = "STOCK"
        const val FAV_ETF = "ETF"
        const val NOT_FAV = "XXX"
    }
}
