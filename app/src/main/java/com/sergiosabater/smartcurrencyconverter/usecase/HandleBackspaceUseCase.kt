package com.sergiosabater.smartcurrencyconverter.usecase

import com.sergiosabater.smartcurrencyconverter.util.format.formatDisplay
import com.sergiosabater.smartcurrencyconverter.util.format.formatWithThousandsSeparator

class HandleBackspaceUseCase {

    fun execute(input: String): String {
        val updatedInput = removeLastCharacter(input)
        return formatDisplay(updatedInput)
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

}