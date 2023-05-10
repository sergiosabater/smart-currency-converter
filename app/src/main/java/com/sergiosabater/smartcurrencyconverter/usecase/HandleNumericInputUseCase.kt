package com.sergiosabater.smartcurrencyconverter.usecase

import com.sergiosabater.smartcurrencyconverter.util.format.formatWithThousandsSeparator
import com.sergiosabater.smartcurrencyconverter.util.format.updateAndFormatDisplay

class HandleNumericInputUseCase {

    fun execute(currentDisplay: String, digit: String): String {
        return updateAndFormatDisplay(currentDisplay, digit)
    }
}