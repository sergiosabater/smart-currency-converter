package com.sergiosabater.smartcurrencyconverter.util.parser

import android.content.Context
import com.google.gson.JsonParser
import com.sergiosabater.smartcurrencyconverter.R
import com.sergiosabater.smartcurrencyconverter.model.Currency
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Obtiene la lista de monedas a partir de un archivo JSON de recursos y sus tasas de conversión.
 *
 * Esta función lee la información de monedas y sus tasas de conversión a partir de dos archivos JSON
 * de recursos. Luego crea una lista de objetos [Currency] con los datos obtenidos.
 *
 * @param context El [Context] utilizado para acceder a los recursos de la aplicación.
 * @return Una lista de objetos [Currency] con los datos de monedas y sus tasas de conversión.
 */
fun parseCurrencies(context: Context): List<Currency> {
    val jsonString = readRawResource(context, R.raw.currency_dictionary)

    val jsonElement = JsonParser.parseString(jsonString)
    val jsonObject = jsonElement.asJsonObject

    val exchangeRates = readConversionRates(context, R.raw.currency_rates)

    return jsonObject.entrySet().mapNotNull { entry ->
        val currencyIsoCode = entry.key
        val currencyInfo = entry.value.asJsonObject

        val currencyName = currencyInfo.get("currency_name")?.asString
        val countryName = currencyInfo.get("country")?.asString
        val currencySymbol = currencyInfo.get("currency_symbol")?.asString
        val exchangeRate = exchangeRates[currencyIsoCode]

        if (currencyName != null && countryName != null && exchangeRate != null && currencySymbol != null) {
            Currency(
                isoCode = currencyIsoCode,
                countryName = countryName,
                currencyName = currencyName,
                exchangeRate = exchangeRate,
                currencySymbol = currencySymbol
            )
        } else {
            null
        }
    }
}


/**
 * Lee el contenido de un archivo y lo devuelve como [String].
 *
 * @param context El [Context] utilizado para acceder a los recursos de la aplicación.
 * @param rawResourceId El ID de recurso del archivo.
 * @return El contenido del archivo como [String].
 */
private fun readRawResource(context: Context, rawResourceId: Int): String {
    val inputStream = context.resources.openRawResource(rawResourceId)
    val reader = BufferedReader(InputStreamReader(inputStream))
    return reader.use { it.readText() }
}


/**
 * Obtiene las tasas de conversión de monedas a partir de un archivo JSON de recursos.
 *
 * Esta función lee las tasas de conversión de monedas desde un archivo JSON de recursos y crea
 * un mapa con los códigos ISO de las monedas como clave y sus tasas de conversión como valor.
 *
 * @param context El [Context] utilizado para acceder a los recursos de la aplicación.
 * @param rawResourceId El ID de recurso del archivo JSON que contiene las tasas de conversión.
 * @return Un mapa con las tasas de conversión de monedas, donde las claves son los códigos ISO y
 * los valores son las tasas de conversión.
 */
private fun readConversionRates(context: Context, rawResourceId: Int): Map<String, Double> {
    val jsonString = readRawResource(context, rawResourceId)
    val jsonElement = JsonParser.parseString(jsonString)
    val jsonObject = jsonElement.asJsonObject

    val ratesJson = jsonObject.getAsJsonObject("rates")
    val rates = mutableMapOf<String, Double>()

    ratesJson.entrySet().forEach { entry ->
        val isoCode = entry.key
        val rate = entry.value.asDouble

        rates[isoCode] = rate
    }

    return rates
}