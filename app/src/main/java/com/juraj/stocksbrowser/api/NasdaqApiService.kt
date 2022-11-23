package com.juraj.stocksbrowser.api

import com.juraj.stocksbrowser.dao.StockApiResponse
import retrofit2.http.GET

interface NasdaqApiService {

    @GET("api/screener/stocks?download=true")
    suspend fun getStocks(): StockApiResponse?

    @GET("api/screener/etf?download=true")
    suspend fun getEtfs(): StockApiResponse? // TODO: new data model
}