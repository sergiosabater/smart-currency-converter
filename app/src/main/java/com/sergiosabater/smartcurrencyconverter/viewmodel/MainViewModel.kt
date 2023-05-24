package com.sergiosabater.smartcurrencyconverter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sergiosabater.smartcurrencyconverter.data.network.ApiResult
import com.sergiosabater.smartcurrencyconverter.domain.model.Currency
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyResult
import com.sergiosabater.smartcurrencyconverter.domain.usecase.common.HandleConversionUseCase
import com.sergiosabater.smartcurrencyconverter.domain.usecase.common.NavigateToSettingsUseCase
import com.sergiosabater.smartcurrencyconverter.domain.usecase.currencySelector.HandleCurrencySelectionUseCase
import com.sergiosabater.smartcurrencyconverter.domain.usecase.display.HandleClearDisplayUseCase
import com.sergiosabater.smartcurrencyconverter.domain.usecase.keyboard.HandleBackspaceUseCase
import com.sergiosabater.smartcurrencyconverter.domain.usecase.keyboard.HandleNumericInputUseCase
import com.sergiosabater.smartcurrencyconverter.domain.usecase.keyboard.PlaySoundUseCase
import com.sergiosabater.smartcurrencyconverter.repository.CurrencyRepository
import com.sergiosabater.smartcurrencyconverter.util.constant.NumberConstants.INITIAL_VALUE_STRING
import com.sergiosabater.smartcurrencyconverter.util.constant.SymbolConstants.AMERICAN_DOLLAR
import com.sergiosabater.smartcurrencyconverter.util.constant.SymbolConstants.EURO
import com.sergiosabater.smartcurrencyconverter.util.constant.TextConstants.AMERICAN_DOLLAR_ISO_CODE
import com.sergiosabater.smartcurrencyconverter.util.constant.TextConstants.EURO_ISO_CODE
import com.sergiosabater.smartcurrencyconverter.util.parser.CurrencyApiHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val currencyRepository: CurrencyRepository,
    private val navigateToSettingsUseCase: NavigateToSettingsUseCase,
    private val currencyApiHelper: CurrencyApiHelper
) :
    AndroidViewModel(application) {
    // Los casos de uso que manejan la lógica de negocio y son instanciados en el ViewModel
    private val handleClearDisplayUseCase = HandleClearDisplayUseCase()
    private val handleNumericInputUseCase = HandleNumericInputUseCase()
    private val handleBackspaceUseCase = HandleBackspaceUseCase()
    private val handleCurrencySelectionUseCase = HandleCurrencySelectionUseCase()
    private val handleConversionUseCase = HandleConversionUseCase()
    private val playSoundUseCase = PlaySoundUseCase(application)

    private val settingsViewModel: SettingsViewModel = SettingsViewModel(application)

    private var isSoundEnabled = false


    // Los StateFlows para manejar el estado de las vistas
    private val _currencies = MutableStateFlow<CurrencyResult>(CurrencyResult.Loading)
    val currencies: StateFlow<CurrencyResult> = _currencies

    internal val _displayText = MutableStateFlow(INITIAL_VALUE_STRING)
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
        initUserPreferences()
    }

    private fun initUserPreferences() {
        viewModelScope.launch {
            settingsViewModel.userPreferencesFlow.collect { value ->
                isSoundEnabled = value.soundEnabled
            }
        }
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
    internal fun loadCurrencies() {

        // La función launch inicia una nueva corrutina en 'viewModelScope'.
        // Este scope está ligado al ciclo de vida del ViewModel.

        _currencies.value = CurrencyResult.Loading

        viewModelScope.launch {
            when (val response = currencyRepository.getCurrencyRates()) {
                is ApiResult.Success -> {
                    val currenciesList =
                        currencyApiHelper.loadCurrenciesFromApi(getApplication(), response.data)
                    _currencies.value = CurrencyResult.Success(currenciesList)

                    // Buscamos en la lista de monedas la que tenga el código ISO igual a 'EURO_ISO_CODE'
                    // y la asignamos a '_selectedCurrency1'. Si no se encuentra ninguna,
                    // se asigna la primera moneda de la lista o null si la lista está vacía.
                    _selectedCurrency1.value =
                        (currencies.value as? CurrencyResult.Success)?.data?.find { it.isoCode == EURO_ISO_CODE }
                            ?: (currencies.value as? CurrencyResult.Success)?.data?.firstOrNull()

                    _selectedCurrency2.value =
                        (currencies.value as? CurrencyResult.Success)?.data?.find { it.isoCode == AMERICAN_DOLLAR_ISO_CODE }
                            ?: (currencies.value as? CurrencyResult.Success)?.data?.firstOrNull()
                }

                is ApiResult.Error -> {
                    _currencies.value = CurrencyResult.Failure(Exception(response.exception))
                }
            }
        }

    }

    // Este método se utiliza para iniciar la conversión después de un pequeño retardo.
    // para evitar conversiones innecesarias
    private fun triggerConversion() {
        // Corutina dentro del viewModelScope. La corutina se cancelará cuando
        // se destruya el ViewModel.
        viewModelScope.launch {
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

    fun onSettingsButtonClicked() {
        navigateToSettingsUseCase.execute()
    }

    fun onKeyClicked(keyText: String) {
        playSoundUseCase.play(keyText, isSoundEnabled)
    }

}
