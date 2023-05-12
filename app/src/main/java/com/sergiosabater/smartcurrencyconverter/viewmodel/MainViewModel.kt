package com.sergiosabater.smartcurrencyconverter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sergiosabater.smartcurrencyconverter.model.Currency
import com.sergiosabater.smartcurrencyconverter.usecase.ClearDisplayUseCase
import com.sergiosabater.smartcurrencyconverter.usecase.HandleBackspaceUseCase
import com.sergiosabater.smartcurrencyconverter.usecase.HandleConversionUseCase
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
    private val handleConversionUseCase = HandleConversionUseCase()

    private val _displayText = MutableStateFlow(INITIAL_VALUE_STRING)
    val displayText: StateFlow<String> = _displayText

    private val _displaySymbol = MutableStateFlow("€")
    val displaySymbol: StateFlow<String> = _displaySymbol

    private var selectedCurrency1 = MutableStateFlow<Currency?>(null)
    private var selectedCurrency2 = MutableStateFlow<Currency?>(null)

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

    fun onCurrencySelected(selectedCurrency1: Currency, selectedCurrency2: Currency){
        this.selectedCurrency1.value = selectedCurrency1
        this.selectedCurrency2.value = selectedCurrency2
        _displaySymbol.value = handleCurrencySelectionUseCase.execute(selectedCurrency1)
    }

    fun onConversionButtonClicked() {
        // Comprueba que ambas monedas y la cantidad a convertir no sean nulas
        if (selectedCurrency1.value != null && selectedCurrency2.value != null && _displayText.value.isNotEmpty()) {
            handleConversionUseCase.execute(
                selectedCurrency1.value!!,
                selectedCurrency2.value!!,
                _displayText.value
            )
        }
    }
}