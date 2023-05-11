package com.sergiosabater.smartcurrencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import com.sergiosabater.smartcurrencyconverter.model.Currency
import com.sergiosabater.smartcurrencyconverter.usecase.ClearDisplayUseCase
import com.sergiosabater.smartcurrencyconverter.usecase.HandleBackspaceUseCase
import com.sergiosabater.smartcurrencyconverter.usecase.HandleCurrencySelectionUseCase
import com.sergiosabater.smartcurrencyconverter.usecase.HandleNumericInputUseCase
import com.sergiosabater.smartcurrencyconverter.util.constant.NumberConstants.INITIAL_VALUE_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val clearDisplayUseCase = ClearDisplayUseCase()
    private val handleNumericInputUseCase = HandleNumericInputUseCase()
    private val handleBackspaceUseCase = HandleBackspaceUseCase()
    private val handleCurrencySelectionUseCase = HandleCurrencySelectionUseCase()

    private val _displayText =
        MutableStateFlow(INITIAL_VALUE_STRING) // Valor que comienza por defecto
    val displayText: StateFlow<String> = _displayText //Reemplazar String por Sealed Class

    private val _displaySymbol = MutableStateFlow("€") // Valor inicial
    val displaySymbol: StateFlow<String> = _displaySymbol

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