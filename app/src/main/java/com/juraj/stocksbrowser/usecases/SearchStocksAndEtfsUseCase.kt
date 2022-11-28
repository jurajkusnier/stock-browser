package com.juraj.stocksbrowser.usecases

import com.juraj.stocksbrowser.repositories.EtfRepository
import com.juraj.stocksbrowser.repositories.StocksRepository
import com.juraj.stocksbrowser.ui.home.screen.ListItem
import com.juraj.stocksbrowser.ui.home.screen.toInstrumentItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchStocksAndEtfsUseCase @Inject constructor(
    private val stocksRepository: StocksRepository,
    private val etfRepository: EtfRepository
) {

    operator fun invoke(text: String): Flow<List<ListItem.InstrumentItem>> {
        return stocksRepository
            .findStocks(text).map { it.map { item -> item.toInstrumentItem() } }
            .combine(
                etfRepository.findEtf(text)
                    .map { it.map { item -> item.toInstrumentItem() } }) { stocks, etfs ->
                stocks + etfs
            }
    }

}