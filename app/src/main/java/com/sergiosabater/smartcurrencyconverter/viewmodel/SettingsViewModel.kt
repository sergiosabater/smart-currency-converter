package com.sergiosabater.smartcurrencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergiosabater.smartcurrencyconverter.repository.UserPreferences
import com.sergiosabater.smartcurrencyconverter.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {

    val userPreferencesFlow: StateFlow<UserPreferences> =
        userPreferencesRepository.userPreferencesFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UserPreferences(false))

    fun updateSoundEnabled(soundEnabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateSoundEnabled(soundEnabled)
        }
    }
}