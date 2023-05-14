package com.sergiosabater.smartcurrencyconverter.usecase.common

import android.util.Log
import com.sergiosabater.smartcurrencyconverter.model.Currency
import com.sergiosabater.smartcurrencyconverter.util.conversion.convertCurrencyAmount
import com.sergiosabater.smartcurrencyconverter.util.format.formatNumber
import java.math.BigDecimal

class HandleConversionUseCase {

    fun execute(
        currency1: Currency,
        currency2: Currency,
        amount: String
    ): String {
        // Eliminar los puntos de miles si los hay
        val cleanAmount = amount.replace(".", "")

        // Reemplazar la coma por un punto
        val decimalAmount = cleanAmount.replace(",", ".")

        // Convierte la cantidad a BigDecimal
        val bigDecimalAmount = BigDecimal(decimalAmount)

        // Aquí puedo realizar la conversión de moneda y retornar el resultado
        // Calcula el resultado de la conversión
        val conversionResult = convertCurrencyAmount(
            currency1.exchangeRate,
            currency2.exchangeRate,
            bigDecimalAmount.toDouble()
        )

        val logMessage =
            "$conversionResult"
        Log.d("Conversion", logMessage)

        return formatNumber(conversionResult.toString())
    }
}
