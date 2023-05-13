package com.sergiosabater.smartcurrencyconverter.usecase

import com.sergiosabater.smartcurrencyconverter.util.format.formatDisplay
import com.sergiosabater.smartcurrencyconverter.util.format.updateDisplay


class HandleNumericInputUseCase {

    fun execute(currentDisplay: String, digit: String): String {
        val updatedDisplay = updateDisplay(currentDisplay, digit)
        return formatDisplay(updatedDisplay)
    }
}