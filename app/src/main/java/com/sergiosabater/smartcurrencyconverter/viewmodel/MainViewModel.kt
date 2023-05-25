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
    private val currencyApiHelper: CurrencyApiHelper,
    private val settingsViewModel: SettingsViewModel
) :
    AndroidViewModel(application) {
    // Los casos de uso que manejan la lógica de negocio y son instanciados en el ViewModel
    private val handleClearDisplayUseCase = HandleClearDisplayUseCase()
    private val handleNumericInputUseCase = HandleNumericInputUseCase()
    private val handleBackspaceUseCase = HandleBackspaceUseCase()
    private val handleCurrencySelectionUseCase = HandleCurrencySelectionUseCase()
    private val handleConversionUseCase = HandleConversionUseCase()
    private val playSoundUseCase = PlaySoundUseCase(application)

    private var isSoundEnabled = false

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
            conversionSymbol = AMERICAN_DOLLAR
        )
    )
    val uiState: StateFlow<UIState> = _uiState

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
        _uiState.value = _uiState.value.copy(
            displayText = handleClearDisplayUseCase.execute()
        )
        triggerConversion()
    }

    fun onNumericButtonClicked(input: String) {
        _uiState.value = _uiState.value.copy(
            displayText = handleNumericInputUseCase.execute(_uiState.value.displayText, input)
        )
        triggerConversion()
    }

    fun onBackspaceClicked() {
        val updatedInput = handleBackspaceUseCase.execute(_uiState.value.displayText)
        _uiState.value = _uiState.value.copy(displayText = updatedInput)
        triggerConversion()
    }

    fun onCurrencySelected(selectedCurrency1: Currency, selectedCurrency2: Currency) {
        _uiState.value = _uiState.value.copy(
            selectedCurrency1 = selectedCurrency1,
            selectedCurrency2 = selectedCurrency2,
            displaySymbol = handleCurrencySelectionUseCase.execute(selectedCurrency1)
        )
        triggerConversion()
    }

    internal fun loadCurrencies() {
        _uiState.value = _uiState.value.copy(currencies = CurrencyResult.Loading)

        viewModelScope.launch {
            when (val response = currencyRepository.getCurrencyRates()) {
                is ApiResult.Success -> {
                    val currenciesList =
                        currencyApiHelper.loadCurrenciesFromApi(getApplication(), response.data)
                    _uiState.value = _uiState.value.copy(currencies = CurrencyResult.Success(currenciesList))

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
                    _uiState.value = _uiState.value.copy(currencies = CurrencyResult.Failure(response.exception))
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
            val conversionResult = handleConversionUseCase.execute(
                _uiState.value.selectedCurrency1!!,
                _uiState.value.selectedCurrency2!!,
                _uiState.value.displayText
            )
            return Pair(conversionResult, _uiState.value.selectedCurrency2?.currencySymbol ?: "")
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

// Clase data para agrupar el estado de la UI
data class UIState(
    val currencies: CurrencyResult,
    val displayText: String,
    val displaySymbol: String,
    val selectedCurrency1: Currency?,
    val selectedCurrency2: Currency?,
    val conversionResult: String,
    val conversionSymbol: String
)

