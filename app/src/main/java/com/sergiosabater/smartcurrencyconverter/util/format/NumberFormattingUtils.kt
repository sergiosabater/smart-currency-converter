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


/**
 * Actualiza el contenido del display con el nuevo dígito ingresado y lo formatea
 * con separadores de miles
 * @param currentDisplay el contenido actual del display
 * @param digit el dígito ingresado por el usuario
 * @return el nuevo contenido del display con separadores de miles
 */
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
    } else if (integerPart == "") {
        newDisplay = "0"
    } else {
        val formattedIntegerPart = formatWithThousandsSeparator(integerPart)
        newDisplay =
            if (decimalPart.isNotEmpty()) "$formattedIntegerPart,$decimalPart" else formattedIntegerPart
    }

    return newDisplay
}