package com.sergiosabater.smartcurrencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import com.sergiosabater.smartcurrencyconverter.usecase.KeyboardUseCase
import com.sergiosabater.smartcurrencyconverter.utils.constants.NumberConstants.INITIAL_VALUE_STRING
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {


    private val keyboardUseCase = KeyboardUseCase()
    private val _displayText =
        MutableStateFlow(INITIAL_VALUE_STRING) //Valor que comienza por defecto
    val displayText: StateFlow<String> = _displayText

    fun onClearButtonClicked() {
        _displayText.value = keyboardUseCase.clearDisplay()
    }

    fun onNumericButtonClicked(input: String) {
        _displayText.value = keyboardUseCase.handleNumericInput(_displayText.value, input)
    }

    fun onBackspaceClicked() {
        val updatedInput = keyboardUseCase.handleBackspace(_displayText.value)
        _displayText.value = updatedInput
    }
}