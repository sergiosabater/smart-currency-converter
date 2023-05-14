package com.sergiosabater.smartcurrencyconverter.usecase.common

import com.sergiosabater.smartcurrencyconverter.model.Currency
import com.sergiosabater.smartcurrencyconverter.util.conversion.convertCurrencyAmount
import com.sergiosabater.smartcurrencyconverter.util.format.formatNumber
import java.math.BigDecimal

/**
 * Caso de uso que se encarga de gestionar la conversión de moneda.
 */
class HandleConversionUseCase {

    /**
     * Realiza la conversión de moneda.
     *
     * @param currency1 La primera moneda en la transacción de cambio.
     * @param currency2 La segunda moneda en la transacción de cambio.
     * @param amount La cantidad de la moneda1 que se va a convertir a la moneda2.
     * @return Una cadena de texto que representa la cantidad convertida en la moneda2.
     */
    fun execute(
        currency1: Currency,
        currency2: Currency,
        amount: String
    ): String {
        // Limpia la cadena
        val cleanedAmount = cleanInputAmount(amount)

        // Convierte la cantidad a BigDecimal
        val bigDecimalAmount = BigDecimal(cleanedAmount)

        // Realiza la conversión de moneda y retorna el resultado
        val conversionResult = convertCurrencyAmount(
            currency1.exchangeRate,
            currency2.exchangeRate,
            bigDecimalAmount.toDouble()
        )

        return formatNumber(conversionResult.toString())
    }

    /**
     * Limpia la entrada del usuario eliminando los puntos de miles
     * y reemplazando las comas por puntos.
     *
     * @param amount La cantidad de moneda ingresada por el usuario.
     * @return Una cadena de texto que representa la cantidad de moneda ingresada
     * sin puntos de miles y con las comas reemplazadas por puntos.
     */
    private fun cleanInputAmount(amount: String): String {
        // Elimina los puntos de miles si los hay
        val cleanAmount = amount.replace(".", "")

        // Reemplaza la coma por un punto
        return cleanAmount.replace(",", ".")
    }
}


