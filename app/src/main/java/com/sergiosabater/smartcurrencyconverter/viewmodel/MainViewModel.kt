package com.sergiosabater.smartcurrencyconverter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sergiosabater.smartcurrencyconverter.model.Currency
import com.sergiosabater.smartcurrencyconverter.usecase.common.HandleConversionUseCase
import com.sergiosabater.smartcurrencyconverter.usecase.currencySelector.HandleCurrencySelectionUseCase
import com.sergiosabater.smartcurrencyconverter.usecase.display.HandleClearDisplayUseCase
import com.sergiosabater.smartcurrencyconverter.usecase.keyboard.HandleBackspaceUseCase
import com.sergiosabater.smartcurrencyconverter.usecase.keyboard.HandleNumericInputUseCase
import com.sergiosabater.smartcurrencyconverter.util.constant.NumberConstants.INITIAL_VALUE_STRING
import com.sergiosabater.smartcurrencyconverter.util.parser.parseCurrencies
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val handleClearDisplayUseCase = HandleClearDisplayUseCase()
    private val handleNumericInputUseCase = HandleNumericInputUseCase()
    private val handleBackspaceUseCase = HandleBackspaceUseCase()
    private val handleCurrencySelectionUseCase = HandleCurrencySelectionUseCase()
    private val handleConversionUseCase = HandleConversionUseCase()

    private val _displayText = MutableStateFlow(INITIAL_VALUE_STRING)
    val displayText: StateFlow<String> = _displayText

    private val _displaySymbol = MutableStateFlow("€")
    val displaySymbol: StateFlow<String> = _displaySymbol

    private val _selectedCurrency1 = MutableStateFlow<Currency?>(null)
    val selectedCurrency1: StateFlow<Currency?> = _selectedCurrency1

    private val _selectedCurrency2 = MutableStateFlow<Currency?>(null)
    val selectedCurrency2: StateFlow<Currency?> = _selectedCurrency2

    // Añadir las nuevas MutableStateFlow para el resultado de la conversión y el símbolo de moneda
    private val _conversionResult = MutableStateFlow(INITIAL_VALUE_STRING)
    val conversionResult: StateFlow<String> = _conversionResult

    private val _conversionSymbol = MutableStateFlow("€")
    val conversionSymbol: StateFlow<String> = _conversionSymbol

    // Lista de monedas como StateFlow
    val currencies = MutableStateFlow<List<Currency>>(emptyList())

    init {
        // Inicia la carga de monedas en un hilo de background
        viewModelScope.launch {
            val currencyList = async { parseCurrencies(getApplication()) }
            currencies.value = currencyList.await()

            // Establecer Euro y Dólar estadounidense como las monedas seleccionadas por defecto
            _selectedCurrency1.value = currencies.value.find { it.isoCode == "EUR" }
            _selectedCurrency2.value = currencies.value.find { it.isoCode == "USD" }
        }
    }

    fun onClearButtonClicked() {
        _displayText.value = handleClearDisplayUseCase.execute()
        triggerConversion()
    }

    fun onNumericButtonClicked(input: String) {
        _displayText.value = handleNumericInputUseCase.execute(_displayText.value, input)
        // Lanzar una corrutina para ejecutar la conversión después de un segundo
        triggerConversion()
    }

    fun onBackspaceClicked() {
        val updatedInput = handleBackspaceUseCase.execute(_displayText.value)
        _displayText.value = updatedInput
        triggerConversion()
    }

    fun onCurrencySelected(selectedCurrency1: Currency, selectedCurrency2: Currency) {
        this._selectedCurrency1.value = selectedCurrency1
        this._selectedCurrency2.value = selectedCurrency2
        _displaySymbol.value = handleCurrencySelectionUseCase.execute(selectedCurrency1)
        triggerConversion()
    }

    fun onConversionPerform(): Pair<String, String>? {
        // Comprueba que ambas monedas y la cantidad a convertir no sean nulas
        if (_selectedCurrency1.value != null && _selectedCurrency2.value != null && _displayText.value.isNotEmpty()) {
            val conversionResult = handleConversionUseCase.execute(
                _selectedCurrency1.value!!,
                _selectedCurrency2.value!!,
                _displayText.value
            )
            return Pair(conversionResult, _selectedCurrency2.value?.currencySymbol ?: "")
        }
        return null
    }

    private fun triggerConversion() {
        // Corutina dentro del viewModelScope. La corutina se cancelará cuando
        // se destruya el ViewModel.
        viewModelScope.launch {
            // La función delay() suspende la corutina durante un tiempo determinado
            // por timeMillis
            delay(500)
            // Llamamos a onConversionButtonClicked() para realizar
            // la conversión y almacenamos el resultado en la variable conversionResult.

            // Esto notificará a todos los observadores de _conversionResult que estos valores
            // han cambiado.
            val result = onConversionPerform()
            if (result != null) {
                _conversionResult.value = result.first
                _conversionSymbol.value = result.second
            }
        }
    }
}