package com.sergiosabater.smartcurrencyconverter.util.format

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale


fun formatDisplay(input: String): String {
    // Divide el contenido del input en dos partes (entera y decimal) usando la "," como separador
    val parts = input.split(",")

    // La parte entera es el primer elemento de la lista dividida
    val integerPart = parts[0]

    // Si hay una parte decimal, se guarda en decimalPart; de lo contrario, se guarda una cadena vacía
    val decimalPart = if (parts.size > 1) parts[1] else ""

    // Formateamos la parte entera con separadores de miles
    val formattedIntegerPart = formatWithThousandsSeparator(integerPart)

    // Si hay una parte decimal, se concatena al final de la parte entera formateada
    // De lo contrario, solo se guarda la parte entera formateada en updatedInput
    val updatedInput =
        if (decimalPart.isNotEmpty()) "$formattedIntegerPart,$decimalPart" else formattedIntegerPart

    return updatedInput
}

fun formatWithThousandsSeparator(number: String): String {
    // Elimina los separadores de miles actuales (si los hay) y convierte la cadena de números a un BigInteger
    val integerPart = number.replace(".", "").toBigInteger()

    // Crea una instancia de DecimalFormat con un patrón que define cómo se deben agrupar los números
    val formatter = DecimalFormat("#,###")

    // Crea una instancia de DecimalFormatSymbols para personalizar los símbolos utilizados en el formateo
    val decimalFormatSymbols = DecimalFormatSymbols()

    // Establece el separador de agrupamiento (en este caso, el punto) en DecimalFormatSymbols
    decimalFormatSymbols.groupingSeparator = '.'

    // Asigna la instancia personalizada de DecimalFormatSymbols a la instancia de DecimalFormat
    formatter.decimalFormatSymbols = decimalFormatSymbols

    // Retorna el número formateado con separadores de miles (puntos) usando el formateador configurado
    return formatter.format(integerPart)
}

fun updateAndFormatDisplay(currentDisplay: String, digit: String): String {
    if (digit == "0" && currentDisplay == "0") {
        return currentDisplay
    }

    if (digit == "," && currentDisplay.contains(",")) {
        return currentDisplay
    }

    var newDisplay =
        if (currentDisplay == "0" && digit != ",") digit else currentDisplay + digit

    val parts = newDisplay.split(",")

    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1] else ""

    if (digit == "," && decimalPart.isEmpty()) {
        newDisplay = "$integerPart,"
    } else {
        val formattedIntegerPart = formatWithThousandsSeparator(integerPart)
        newDisplay =
            if (decimalPart.isNotEmpty()) "$formattedIntegerPart,$decimalPart" else formattedIntegerPart
    }

    return newDisplay
}