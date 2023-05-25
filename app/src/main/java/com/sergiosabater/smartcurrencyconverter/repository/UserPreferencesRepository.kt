package com.sergiosabater.smartcurrencyconverter.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateSoundEnabled(soundEnabled: Boolean)
}