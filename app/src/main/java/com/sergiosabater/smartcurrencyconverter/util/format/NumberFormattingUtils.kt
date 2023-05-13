package com.sergiosabater.smartcurrencyconverter.util.format

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 * Devuelve una cadena de texto que representa un número con separadores de miles.
 * @param number el número a formatear como una cadena de texto.
 * @return el número formateado con separadores de miles.
 */
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

fun formatNumber(input: String): String {
    // Reemplaza los puntos por comas
    val replaceDot = input.replace(".", ",")

    // Separa la parte entera y decimal
    val parts = replaceDot.split(",")

    // Formatea la parte entera con separador de miles
    val integerPart = formatWithThousandsSeparator(parts[0])

    //Si la parte entera y la parte decimal son cero, devolverá la parte entera
    return if(integerPart == "0" && parts[1] == "0") {
        integerPart
    } else {
        if (parts.size > 1) "$integerPart,${parts[1]}" else integerPart
    }

}


fun updateDisplay(currentDisplay: String, digit: String): String {
    // si se presionó '0' y el display actual es '0', retorna el display actual
    if (digit == "0" && currentDisplay == "0") {
        return currentDisplay
    }

    // si se presionó ',' y ya existe una ',' en el display actual, retorna el display actual
    if (digit == "," && currentDisplay.contains(",")) {
        return currentDisplay
    }

    // si el display actual es '0' y el dígito presionado no es ',',
    // retorna el dígito presionado; de lo contrario, añade el dígito al display actual
    return if (currentDisplay == "0" && digit != ",") digit else currentDisplay + digit
}

fun formatDisplay(newDisplay: String): String {
    val parts = newDisplay.split(",")
    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1] else ""
    val formattedIntegerPart = formatWithThousandsSeparator(integerPart)
    return if (decimalPart.isNotEmpty() || newDisplay.endsWith(",")) "$formattedIntegerPart,$decimalPart" else formattedIntegerPart
}
