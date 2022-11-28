package com.juraj.stocksbrowser.repositories

import com.juraj.stocksbrowser.dao.StocksDao
import com.juraj.stocksbrowser.model.room.StockEntity
import com.juraj.stocksbrowser.utils.toSafeString
import javax.inject.Inject

class StocksRepository @Inject constructor(
    private val dao: StocksDao
) {

    suspend fun insertStock(stockEntity: StockEntity) =
        dao.insert(stockEntity)

    fun getStocks(symbols: List<String>) =
        dao.getStocksBySymbols(symbols)

    fun getStock(symbol: String) =
        dao.getStockBySymbol(symbol)

    fun findStocks(searchQuery: String) =
        dao.findStocks(searchQuery.toSafeString() + "%")

}