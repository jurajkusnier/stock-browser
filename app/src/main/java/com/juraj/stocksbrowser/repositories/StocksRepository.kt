package com.juraj.stocksbrowser.repositories

import com.juraj.stocksbrowser.data.StockEntity
import com.juraj.stocksbrowser.data.StocksDao
import com.juraj.stocksbrowser.utils.toSafeString
import javax.inject.Inject

class StocksRepository @Inject constructor(
    private val stocksDao: StocksDao
) {

    suspend fun insertStock(stockEntity: StockEntity) =
        stocksDao.insert(stockEntity)

    fun getStocks(symbols: List<String>) =
        stocksDao.getStocksBySymbols(symbols)

    fun getStock(symbol: String) =
        stocksDao.getStockBySymbol(symbol)

    fun findStocks(searchQuery: String) =
        stocksDao.findStocks(searchQuery.toSafeString() + "%")

}