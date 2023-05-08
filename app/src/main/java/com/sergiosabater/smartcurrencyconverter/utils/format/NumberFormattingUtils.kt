package com.sergiosabater.smartcurrencyconverter.utils.format

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

//Función que agrega separadores de miles a un número
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