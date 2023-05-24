package com.sergiosabater.smartcurrencyconverter.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sergiosabater.smartcurrencyconverter.domain.usecase.common.NavigateToSettingsUseCase
import com.sergiosabater.smartcurrencyconverter.repository.CurrencyRepository
import com.sergiosabater.smartcurrencyconverter.util.parser.CurrencyApiHelper

class MainViewModelFactory(
    private val application: Application,
    private val currencyRepository: CurrencyRepository,
    private val navigateToSettingsUseCase: NavigateToSettingsUseCase,
    private val currencyApiHelper: CurrencyApiHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                application,
                currencyRepository,
                navigateToSettingsUseCase,
                currencyApiHelper
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}