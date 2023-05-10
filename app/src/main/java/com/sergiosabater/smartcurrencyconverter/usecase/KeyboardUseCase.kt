package com.sergiosabater.smartcurrencyconverter.usecase

import com.sergiosabater.smartcurrencyconverter.util.format.formatWithThousandsSeparator

class KeyboardUseCase {

    fun clearDisplay(): String {
        return "0"
    }

    fun handleNumericInput(currentDisplay: String, digit: String): String {
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

    fun handleBackspace(input: String): String {
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

        // Divide el contenido del display en dos partes (entera y decimal) usando la "," como separador
        val parts = updatedInput.split(",")

        // La parte entera es el primer elemento de la lista dividida
        val integerPart = parts[0]

        // Si hay una parte decimal, se guarda en decimalPart; de lo contrario, se guarda una cadena vacía
        val decimalPart = if (parts.size > 1) parts[1] else ""

        // Formateamos la parte entera con separadores de miles
        val formattedIntegerPart = formatWithThousandsSeparator(integerPart)

        // Si hay una parte decimal, se concatena al final de la parte entera formateada
        // De lo contrario, solo se guarda la parte entera formateada en updatedInput
        updatedInput =
            if (decimalPart.isNotEmpty()) "$formattedIntegerPart,$decimalPart" else formattedIntegerPart

        // Si el resultado es una cadena vacía, devolvemos "0"; de lo contrario, devolvemos el resultado actualizado
        return if (updatedInput.isEmpty()) "0" else updatedInput
    }
}