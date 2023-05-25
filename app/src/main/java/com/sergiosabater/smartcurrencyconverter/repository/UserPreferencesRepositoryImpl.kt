package com.sergiosabater.smartcurrencyconverter.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferencesRepositoryImpl(context: Context) : UserPreferencesRepository {
    private val dataStore = context.dataStore

    override val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val soundEnabled = preferences[PreferencesKeys.SOUND_ENABLED] ?: false
            UserPreferences(soundEnabled)
        }

    override suspend fun updateSoundEnabled(soundEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] = soundEnabled
        }
    }

    private object PreferencesKeys {
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    }
}

data class UserPreferences(
    val soundEnabled: Boolean,
)
