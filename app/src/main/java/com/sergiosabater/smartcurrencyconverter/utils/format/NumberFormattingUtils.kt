package com.sergiosabater.smartcurrencyconverter.utils.format

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 * Este método toma una cadena de números, elimina los separadores de miles existentes (si los hay),
 * y luego formatea la cadena con separadores de miles utilizando puntos. El método asume que la
 * entrada no contiene decimales.
 *
 * @param number La cadena de números a formatear con separadores de miles.
 * @return La cadena de números formateada con separadores de miles (puntos).
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