package com.juraj.stocksbrowser.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.juraj.stocksbrowser.utils.toLocalDateOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DataStoreWrapper @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun getLocalDate(key: Preferences.Key<String>): LocalDate? {
        return dataStore.data.map { preferences ->
            preferences[key]?.toLocalDateOrNull()
        }.firstOrNull()
    }

    suspend fun setLocalDate(key: Preferences.Key<String>, value: LocalDate) {
        dataStore.edit { preferences ->
            preferences[key] = value.toString()
        }
    }

    fun getStringSet(key: Preferences.Key<Set<String>>): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[key]?.toSet() ?: emptySet()
        }
    }

    suspend fun toggleStringSet(key: Preferences.Key<Set<String>>, value: String) {
        dataStore.edit { preferences ->
            val currentSymbols = preferences[key] ?: emptySet()
            if (currentSymbols.contains(value)) {
                preferences[key] = currentSymbols - setOf(value)
            } else {
                preferences[key] = currentSymbols + setOf(value)
            }
        }
    }
}
