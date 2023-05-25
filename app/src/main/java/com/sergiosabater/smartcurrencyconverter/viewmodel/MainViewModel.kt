package com.sergiosabater.smartcurrencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergiosabater.smartcurrencyconverter.R
import com.sergiosabater.smartcurrencyconverter.data.network.ApiResult
import com.sergiosabater.smartcurrencyconverter.domain.model.Currency
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyResult
import com.sergiosabater.smartcurrencyconverter.domain.usecase.common.NavigateToSettingsUseCase
import com.sergiosabater.smartcurrencyconverter.repository.CurrencyRepository
import com.sergiosabater.smartcurrencyconverter.repository.UserPreferencesRepository
import com.sergiosabater.smartcurrencyconverter.util.constant.NumberConstants.INITIAL_VALUE_STRING
import com.sergiosabater.smartcurrencyconverter.util.constant.SymbolConstants.AMERICAN_DOLLAR
import com.sergiosabater.smartcurrencyconverter.util.constant.SymbolConstants.BACKSPACE_SYMBOL_STRING
import com.sergiosabater.smartcurrencyconverter.util.constant.SymbolConstants.EURO
import com.sergiosabater.smartcurrencyconverter.util.constant.TextConstants.AMERICAN_DOLLAR_ISO_CODE
import com.sergiosabater.smartcurrencyconverter.util.constant.TextConstants.CLEAR_BUTTON_STRING
import com.sergiosabater.smartcurrencyconverter.util.constant.TextConstants.EURO_ISO_CODE
import com.sergiosabater.smartcurrencyconverter.util.conversion.convertCurrencyAmount
import com.sergiosabater.smartcurrencyconverter.util.format.formatDisplay
import com.sergiosabater.smartcurrencyconverter.util.format.formatNumber
import com.sergiosabater.smartcurrencyconverter.util.format.updateDisplay
import com.sergiosabater.smartcurrencyconverter.util.parser.CurrencyLoader
import com.sergiosabater.smartcurrencyconverter.util.sound.SoundPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MainViewModel(
    private val currencyRepository: CurrencyRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val navigateToSettingsUseCase: NavigateToSettingsUseCase,
    private val soundPlayer: SoundPlayer,
    private val currencyLoader: CurrencyLoader
) :
    ViewModel() {

    // Un único StateFlow para manejar el estado de la UI
    private val _uiState = MutableStateFlow(
        UIState(
            //Valores iniciales
            currencies = CurrencyResult.Loading,
            displayText = INITIAL_VALUE_STRING,
            displaySymbol = EURO,
            selectedCurrency1 = null,
            selectedCurrency2 = null,
            conversionResult = INITIAL_VALUE_STRING,
            conversionSymbol = AMERICAN_DOLLAR,
            isSoundEnabled = false
        )
    )
    val uiState: StateFlow<UIState> = _uiState

    init {
        loadCurrencies()
        initUserPreferences()
    }

    private fun initUserPreferences() {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collect { value ->
                _uiState.value = _uiState.value.copy(
                    isSoundEnabled = value.soundEnabled
                )
            }
        }
    }

    // Los métodos siguientes son los eventos que se disparan desde las vistas
    // Cada uno de ellos llama a su respectivo caso de uso
    // y actualiza el estado de la vista correspondiente
    fun onClearButtonClicked() {
        _uiState.value = _uiState.value.copy(
            displayText = "0"
        )
        triggerConversion()
    }

    fun onNumericButtonClicked(input: String) {
        val updatedDisplay = updateDisplay(_uiState.value.displayText, input)
        _uiState.value = _uiState.value.copy(
            displayText = formatDisplay(updatedDisplay)
        )
        triggerConversion()
    }

    fun onBackspaceClicked() {
        val updatedInput = removeLastCharacter(_uiState.value.displayText)
        _uiState.value = _uiState.value.copy(displayText = updatedInput)
        triggerConversion()
    }

    fun onCurrencySelected(selectedCurrency1: Currency, selectedCurrency2: Currency) {
        _uiState.value = _uiState.value.copy(
            selectedCurrency1 = selectedCurrency1,
            selectedCurrency2 = selectedCurrency2,
            displaySymbol = selectedCurrency1.currencySymbol
        )
        triggerConversion()
    }

    internal fun loadCurrencies() {
        _uiState.value = _uiState.value.copy(currencies = CurrencyResult.Loading)

        viewModelScope.launch {
            when (val response = currencyRepository.getCurrencyRates()) {
                is ApiResult.Success -> {
                    val currenciesList =
                        currencyLoader.loadCurrenciesFromApi(response.data)
                    _uiState.value =
                        _uiState.value.copy(currencies = CurrencyResult.Success(currenciesList))

                    _uiState.value = _uiState.value.copy(
                        selectedCurrency1 =
                        _uiState.value.currencies.let { it as? CurrencyResult.Success }?.data?.find { it.isoCode == EURO_ISO_CODE }
                            ?: _uiState.value.currencies.let { it as? CurrencyResult.Success }?.data?.firstOrNull(),

                        selectedCurrency2 =
                        _uiState.value.currencies.let { it as? CurrencyResult.Success }?.data?.find { it.isoCode == AMERICAN_DOLLAR_ISO_CODE }
                            ?: _uiState.value.currencies.let { it as? CurrencyResult.Success }?.data?.firstOrNull()
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value =
                        _uiState.value.copy(currencies = CurrencyResult.Failure(response.exception))
                }
            }
        }
    }

    private fun triggerConversion() {
        viewModelScope.launch {
            val result = onConversionPerform()
            if (result != null) {
                _uiState.value = _uiState.value.copy(
                    conversionResult = result.first,
                    conversionSymbol = result.second
                )
            }
        }
    }

    private fun onConversionPerform(): Pair<String, String>? {
        if (_uiState.value.selectedCurrency1 != null && _uiState.value.selectedCurrency2 != null && _uiState.value.displayText.isNotEmpty()) {
            // Limpia la cadena
            val cleanedAmount = cleanInputAmount(_uiState.value.displayText)

            // Convierte la cantidad a BigDecimal
            val bigDecimalAmount = BigDecimal(cleanedAmount)

            // Realiza la conversión de moneda y retorna el resultado
            val conversionResult = convertCurrencyAmount(
                _uiState.value.selectedCurrency1!!.exchangeRate,
                _uiState.value.selectedCurrency2!!.exchangeRate,
                bigDecimalAmount.toDouble()
            )

            return Pair(
                formatNumber(conversionResult.toString()),
                _uiState.value.selectedCurrency2?.currencySymbol ?: ""
            )
        }
        return null
    }

    private fun removeLastCharacter(input: String): String {
        // Si el input es vacío, solo hay un "0" o solo hay un caracter en el input, devolvemos "0"
        if (input.isEmpty() || input == "0" || input.length == 1) {
            return "0"
        }

        // Si hay más de un caracter, eliminamos el último y devolvemos el resultado
        var updatedInput = input.substring(0, input.length - 1)

        // Si el último caracter es ",", eliminamos ese caracter también
        if (updatedInput.lastOrNull() == ',') {
            updatedInput = updatedInput.substring(0, updatedInput.length - 1)
        }

        return updatedInput
    }

    private fun cleanInputAmount(amount: String): String {
        // Elimina los puntos de miles si los hay
        val cleanAmount = amount.replace(".", "")

        // Reemplaza la coma por un punto
        return cleanAmount.replace(",", ".")
    }

    fun onSettingsButtonClicked() {
        navigateToSettingsUseCase.execute()
    }

    fun onKeyClicked(keyText: String, isSoundEnabled: Boolean) {
        if (isSoundEnabled) {
            val soundResId = when (keyText) {
                CLEAR_BUTTON_STRING, BACKSPACE_SYMBOL_STRING -> R.raw.back_clic_sound
                else -> R.raw.clic_sound
            }
            soundPlayer.playSound(soundResId)
        }
    }
}

// Clase data para agrupar el estado de la UI
data class UIState(
    val currencies: CurrencyResult,
    val displayText: String,
    val displaySymbol: String,
    val selectedCurrency1: Currency?,
    val selectedCurrency2: Currency?,
    val conversionResult: String,
    val conversionSymbol: String,
    val isSoundEnabled: Boolean
)

