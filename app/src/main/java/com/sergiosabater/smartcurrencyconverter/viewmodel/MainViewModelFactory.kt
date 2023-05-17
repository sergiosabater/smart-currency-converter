package com.sergiosabater.smartcurrencyconverter.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sergiosabater.smartcurrencyconverter.domain.usecase.common.NavigateToSettingsUseCase
import com.sergiosabater.smartcurrencyconverter.repository.CurrencyRepository

class MainViewModelFactory(
    private val application: Application,
    private val currencyRepository: CurrencyRepository,
    private val navigateToSettingsUseCase: NavigateToSettingsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application, currencyRepository, navigateToSettingsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}