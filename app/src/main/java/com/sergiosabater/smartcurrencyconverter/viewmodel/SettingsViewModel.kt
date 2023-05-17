package com.sergiosabater.smartcurrencyconverter.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

private const val USER_PREFERENCES_NAME = "user_preferences"

private object PreferencesKeys {
    val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
}

data class UserPreferences(
    val soundEnabled: Boolean,
)

private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = application.dataStore

    val userPreferencesFlow: StateFlow<UserPreferences> = dataStore.data
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
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UserPreferences(false))

    fun updateSoundEnabled(soundEnabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.SOUND_ENABLED] = soundEnabled
            }
        }
    }
}
