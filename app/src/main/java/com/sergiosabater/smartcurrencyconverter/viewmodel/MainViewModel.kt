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
import com.sergiosabater.smartcurrencyconverter.util.constant.SymbolConstants.AMERICAN_DOLLAR
import com.sergiosabater.smartcurrencyconverter.util.constant.SymbolConstants.EURO
import com.sergiosabater.smartcurrencyconverter.util.constant.TextConstants.AMERICAN_DOLLAR_ISO_CODE
import com.sergiosabater.smartcurrencyconverter.util.constant.TextConstants.EURO_ISO_CODE
import com.sergiosabater.smartcurrencyconverter.util.parser.parseCurrencies
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // Los casos de uso que manejan la lógica de negocio y son instanciados en el ViewModel
    private val handleClearDisplayUseCase = HandleClearDisplayUseCase()
    private val handleNumericInputUseCase = HandleNumericInputUseCase()
    private val handleBackspaceUseCase = HandleBackspaceUseCase()
    private val handleCurrencySelectionUseCase = HandleCurrencySelectionUseCase()
    private val handleConversionUseCase = HandleConversionUseCase()

    // Los StateFlows para manejar el estado de las vistas
    val currencies = MutableStateFlow<List<Currency>>(emptyList())

    private val _displayText = MutableStateFlow(INITIAL_VALUE_STRING)
    val displayText: StateFlow<String> = _displayText

    private val _displaySymbol = MutableStateFlow(EURO)
    val displaySymbol: StateFlow<String> = _displaySymbol

    private val _selectedCurrency1 = MutableStateFlow<Currency?>(null)
    val selectedCurrency1: StateFlow<Currency?> = _selectedCurrency1

    private val _selectedCurrency2 = MutableStateFlow<Currency?>(null)
    val selectedCurrency2: StateFlow<Currency?> = _selectedCurrency2

    private val _conversionResult = MutableStateFlow(INITIAL_VALUE_STRING)
    val conversionResult: StateFlow<String> = _conversionResult

    private val _conversionSymbol = MutableStateFlow(AMERICAN_DOLLAR)
    val conversionSymbol: StateFlow<String> = _conversionSymbol

    init {
        loadCurrencies()
    }

    // Los métodos siguientes son los eventos que se disparan desde las vistas
    // Cada uno de ellos llama a su respectivo caso de uso
    // y actualiza el estado de la vista correspondiente
    fun onClearButtonClicked() {
        _displayText.value = handleClearDisplayUseCase.execute()
        triggerConversion()
    }

    fun onNumericButtonClicked(input: String) {
        _displayText.value = handleNumericInputUseCase.execute(_displayText.value, input)
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

    // Este método carga las monedas de manera asíncrona (mediante corrutina)
    // las monedas con los códigos ISO "EUR" y "USD" se seleccionan como
    // las monedas iniciales en el CurrencySelector
    private fun loadCurrencies() {
        // La función launch inicia una nueva corrutina en 'viewModelScope'.
        // Este scope está ligado al ciclo de vida del ViewModel.
        viewModelScope.launch {
            // Utilizamos 'async' para realizar la operación en segundo plano
            val currencyList = async { parseCurrencies(getApplication()) }

            // 'await()' suspende la corrutina hasta que 'async' haya terminado
            currencies.value = currencyList.await()

            // Buscamos en la lista de monedas la que tenga el código ISO igual a 'EURO_ISO_CODE'
            // y la asignamos a '_selectedCurrency1'. Si no se encuentra ninguna,
            // se asigna la primera moneda de la lista o null si la lista está vacía.
            _selectedCurrency1.value = currencies.value.find { it.isoCode == EURO_ISO_CODE }
                ?: currencies.value.firstOrNull()

            // Hacemos lo mismo con '_selectedCurrency2', buscando el código ISO 'AMERICAN_DOLLAR_ISO_CODE'.
            _selectedCurrency2.value =
                currencies.value.find { it.isoCode == AMERICAN_DOLLAR_ISO_CODE }
                    ?: currencies.value.firstOrNull()
        }
    }

    // Este método se utiliza para iniciar la conversión después de un pequeño retardo.
    // para evitar conversiones innecesarias
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

    // Este método realiza la conversión de monedas utilizando el caso de uso
    // y actualiza el estado de la interfaz de usuario correspondiente
    private fun onConversionPerform(): Pair<String, String>? {
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
}