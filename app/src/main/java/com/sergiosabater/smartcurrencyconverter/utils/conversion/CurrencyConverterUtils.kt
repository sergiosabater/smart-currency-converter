package com.sergiosabater.smartcurrencyconverter.utils.conversion

import kotlin.math.round

/**
 * Convierte una cantidad de una divisa a otra divisa utilizando tasas de cambio proporcionadas.
 *
 * @param rateChangeCurrency1 La tasa de cambio de la primera divisa a dólares.
 * @param rateChangeCurrency2 La tasa de cambio de la segunda divisa a dólares.
 * @param amountCurrency1 La cantidad de la primera divisa que se desea convertir a la segunda divisa.
 * @return La cantidad convertida en la segunda divisa, redondeada a 2 decimales.
 */

fun convertCurrencyAmount(
    rateChangeCurrency1: Double,
    rateChangeCurrency2: Double,
    amountCurrency1: Double
): Double {
    // Calcula cuántos dólares se obtienen por 1 unidad de la primera divisa
    val dollarsPerCurrency1 = 1 / rateChangeCurrency1

    // Convierte la cantidad en dólares a la segunda divisa y almacena el resultado en la variable
    // 'conversionRate'
    val conversionRate = dollarsPerCurrency1 * rateChangeCurrency2

    // Multiplica 'conversionRate' por 'amountCurrency1' y redondea el resultado a 2 decimales
    val result = conversionRate * amountCurrency1
    return round(result * 100) / 100
}

