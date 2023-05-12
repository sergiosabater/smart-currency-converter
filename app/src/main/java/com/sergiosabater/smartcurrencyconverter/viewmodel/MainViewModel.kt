package com.sergiosabater.smartcurrencyconverter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sergiosabater.smartcurrencyconverter.model.Currency
import com.sergiosabater.smartcurrencyconverter.usecase.ClearDisplayUseCase
import com.sergiosabater.smartcurrencyconverter.usecase.HandleBackspaceUseCase
import com.sergiosabater.smartcurrencyconverter.usecase.HandleCurrencySelectionUseCase
import com.sergiosabater.smartcurrencyconverter.usecase.HandleNumericInputUseCase
import com.sergiosabater.smartcurrencyconverter.util.constant.NumberConstants.INITIAL_VALUE_STRING
import com.sergiosabater.smartcurrencyconverter.util.parser.parseCurrencies
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val clearDisplayUseCase = ClearDisplayUseCase()
    private val handleNumericInputUseCase = HandleNumericInputUseCase()
    private val handleBackspaceUseCase = HandleBackspaceUseCase()
    private val handleCurrencySelectionUseCase = HandleCurrencySelectionUseCase()

    private val _displayText = MutableStateFlow(INITIAL_VALUE_STRING)
    val displayText: StateFlow<String> = _displayText

    private val _displaySymbol = MutableStateFlow("€")
    val displaySymbol: StateFlow<String> = _displaySymbol

    // Lista de monedas como StateFlow
    val currencies = MutableStateFlow<List<Currency>>(emptyList())

    init {
        // Inicia la carga de monedas en un hilo de background
        viewModelScope.launch {
            val currencyList = async { parseCurrencies(getApplication()) }
            currencies.value = currencyList.await()
        }
    }

    fun onClearButtonClicked() {
        _displayText.value = clearDisplayUseCase.execute()
    }

    fun onNumericButtonClicked(input: String) {
        _displayText.value = handleNumericInputUseCase.execute(_displayText.value, input)
    }

    fun onBackspaceClicked() {
        val updatedInput = handleBackspaceUseCase.execute(_displayText.value)
        _displayText.value = updatedInput
    }

    fun onCurrencySelected(currencies: List<Currency>, currencyName: String) {
        _displaySymbol.value = handleCurrencySelectionUseCase.execute(currencies, currencyName)
    }

}