package com.juraj.stocksbrowser.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    fun getSymbolsUpdateDate() = dataStore.data.map { preferences ->
        preferences[SYMBOLS_UPDATED_AT]?.toLocalDateOrNull()
    }

    suspend fun setSymbolsUpdateDate(localDate: LocalDate) {
        dataStore.edit { preferences ->
            preferences[SYMBOLS_UPDATED_AT] = localDate.toString()
        }
    }

    private val SYMBOLS_UPDATED_AT = stringPreferencesKey("symbols_updated_at")

    private fun String.toLocalDateOrNull(): LocalDate? {
        return try {
            LocalDate.parse(this)
        } catch (e: Exception) {
            null
        }
    }

}