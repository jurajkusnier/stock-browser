package com.juraj.stocksbrowser.repositories

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import java.time.LocalDate
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val store: DataStoreWrapper
) {

    suspend fun getEtfsUpdateDate(): LocalDate? = store.getLocalDate(ETFS_UPDATED_AT)

    suspend fun getStocksUpdateDate(): LocalDate? = store.getLocalDate(STOCKS_UPDATED_AT)

    suspend fun setEtfsUpdateDate(value: LocalDate) = store.setLocalDate(ETFS_UPDATED_AT, value)

    suspend fun setStocksUpdateDate(value: LocalDate) = store.setLocalDate(STOCKS_UPDATED_AT, value)

    fun getFavoritesStocks() = store.getStringSet(FAV_STOCKS)

    fun getFavoritesEtfs() = store.getStringSet(FAV_ETFS)

    suspend fun toggleFavoritesStocks(symbol: String) = store.toggleStringSet(FAV_STOCKS, symbol)

    suspend fun toggleFavoritesEtfs(symbol: String) = store.toggleStringSet(FAV_ETFS, symbol)

    companion object {
        private val STOCKS_UPDATED_AT = stringPreferencesKey("stocks_updated_at")
        private val ETFS_UPDATED_AT = stringPreferencesKey("etfs_updated_at")
        private val FAV_STOCKS = stringSetPreferencesKey("fav_stocks")
        private val FAV_ETFS = stringSetPreferencesKey("fav_etfs")
    }
}
