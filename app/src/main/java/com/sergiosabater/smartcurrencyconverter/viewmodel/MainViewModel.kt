package com.sergiosabater.smartcurrencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import com.sergiosabater.smartcurrencyconverter.usecase.KeyboardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val keyboardUseCase = KeyboardUseCase()
    private val _displayText = MutableStateFlow("12345.67") //Valor que comienza por defecto
    val displayText: StateFlow<String> = _displayText

    fun onClearButtonClicked() {
        _displayText.value = keyboardUseCase.clearDisplay()
    }

    fun onNumericButtonClicked(input: String) {
        _displayText.value = keyboardUseCase.handleNumericInput(_displayText.value, input)
    }
}