package com.juraj.stocksbrowser.api

import com.juraj.stocksbrowser.model.network.EtfApiResponse
import com.juraj.stocksbrowser.model.network.StockApiResponse
import retrofit2.http.GET

interface NasdaqApiService {

    @GET("api/screener/stocks?download=true")
    suspend fun getStocks(): StockApiResponse?

    @GET("api/screener/etf?download=true")
    suspend fun getEtfs(): EtfApiResponse?
}
