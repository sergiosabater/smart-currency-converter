package com.sergiosabater.smartcurrencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sergiosabater.smartcurrencyconverter.repository.CurrencyRepository
import com.sergiosabater.smartcurrencyconverter.repository.UserPreferencesRepository
import com.sergiosabater.smartcurrencyconverter.util.parser.CurrencyLoader
import com.sergiosabater.smartcurrencyconverter.util.sound.SoundPlayer

class MainViewModelFactory(

    private val currencyRepository: CurrencyRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val soundPlayer: SoundPlayer,
    private val currencyLoader: CurrencyLoader,

    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                currencyRepository,
                userPreferencesRepository,
                soundPlayer,
                currencyLoader,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}