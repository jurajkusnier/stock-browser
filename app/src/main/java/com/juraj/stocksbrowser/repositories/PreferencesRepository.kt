package com.juraj.stocksbrowser.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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

    companion object {
        private val SYMBOLS_UPDATED_AT = stringPreferencesKey("symbols_updated_at")
        private val STOCKS_UPDATED_AT = stringPreferencesKey("stocks_updated_at")
    }

}