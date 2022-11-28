package com.juraj.stocksbrowser.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.juraj.stocksbrowser.utils.toLocalDateOrNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun getStocksUpdateDate(): LocalDate? = dataStore.data.map { preferences ->
        preferences[STOCKS_UPDATED_AT]?.toLocalDateOrNull()
    }.firstOrNull()

    suspend fun setStocksUpdateDate(value: LocalDate) {
        dataStore.edit { preferences ->
            preferences[STOCKS_UPDATED_AT] = value.toString()
        }
    }

    fun getSymbolsUpdateDate() = dataStore.data.map { preferences ->
        preferences[SYMBOLS_UPDATED_AT]?.toLocalDateOrNull()
    }

    suspend fun setSymbolsUpdateDate(localDate: LocalDate) {
        dataStore.edit { preferences ->
            preferences[SYMBOLS_UPDATED_AT] = localDate.toString()
        }
    }

    fun getFavoritesStocks() = dataStore.data.map { preferences ->
        preferences[FAV_STOCKS]?.toSet()
    }

    suspend fun toggleFavoritesStocks(symbol:String) {
        dataStore.edit { preferences ->
            val currentSymbols = preferences[FAV_STOCKS] ?: emptySet()
            if (currentSymbols.contains(symbol)) {
                preferences[FAV_STOCKS] = currentSymbols - setOf(symbol)
            } else {
                preferences[FAV_STOCKS] = currentSymbols + setOf(symbol)
            }
        }
    }

    companion object {
        private val SYMBOLS_UPDATED_AT = stringPreferencesKey("symbols_updated_at")
        private val STOCKS_UPDATED_AT = stringPreferencesKey("stocks_updated_at")
        private val FAV_STOCKS = stringSetPreferencesKey("fav_stocks")
    }

}