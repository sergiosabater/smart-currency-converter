package com.sergiosabater.smartcurrencyconverter.usecase

import com.sergiosabater.smartcurrencyconverter.util.format.formatWithThousandsSeparator

class HandleNumericInputUseCase {

    fun execute(currentDisplay: String, digit: String): String {
        val newDisplay = updateDisplay(currentDisplay, digit)
        return newDisplay
    }

    private fun updateDisplay(currentDisplay: String, digit: String): String {
        // Si el dígito ingresado es "0" y el display actual es "0", no se realiza ninguna acción
        if (digit == "0" && currentDisplay == "0") {
            return currentDisplay
        }

        // Si el dígito ingresado es "," y el display ya contiene una ",", no se realiza ninguna acción
        if (digit == "," && currentDisplay.contains(",")) {
            return currentDisplay
        }

        // Si el display es "0" y se ingresa un dígito diferente a ",", se reemplaza el "0" con el dígito
        // En caso contrario, se añade el dígito al display actual
        var newDisplay =
            if (currentDisplay == "0" && digit != ",") digit else currentDisplay + digit

        // Divide el contenido del display en dos partes (entera y decimal) usando la "," como separador
        val parts = newDisplay.split(",")

        // La parte entera es el primer elemento de la lista dividida
        val integerPart = parts[0]

        // Si hay una parte decimal, se guarda en decimalPart; de lo contrario, se guarda una cadena vacía
        val decimalPart = if (parts.size > 1) parts[1] else ""

        // Si se ingresó una "," y no hay parte decimal, se agrega la "," al final de la parte entera
        if (digit == "," && decimalPart.isEmpty()) {
            newDisplay = "$integerPart,"
        } else {
            // De lo contrario, se formatea la parte entera con separadores de miles
            val formattedIntegerPart = formatWithThousandsSeparator(integerPart)

            // Si hay una parte decimal, se concatena al final de la parte entera formateada
            // De lo contrario, solo se guarda la parte entera formateada en newDisplay
            newDisplay =
                if (decimalPart.isNotEmpty()) "$formattedIntegerPart,$decimalPart" else formattedIntegerPart
        }

        // Retorna el nuevo contenido del display
        return newDisplay
    }
}