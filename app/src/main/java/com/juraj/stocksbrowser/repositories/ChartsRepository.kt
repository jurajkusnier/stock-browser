package com.juraj.stocksbrowser.repositories

import com.juraj.stocksbrowser.api.YahooApiService
import com.juraj.stocksbrowser.model.network.ChartApiResult
import javax.inject.Inject

class ChartsRepository @Inject constructor(
    private val yahooApiService: YahooApiService
) {

    suspend fun getChart(symbol: String, interval: String, range: String): ChartApiResult =
        yahooApiService.getChart(symbol = symbol, interval = interval, range = range)
}