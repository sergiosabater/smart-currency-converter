package com.sergiosabater.smartcurrencyconverter.util.parser

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.sergiosabater.smartcurrencyconverter.R
import com.sergiosabater.smartcurrencyconverter.domain.model.Currency
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


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


fun loadCurrenciesFromApi(context: Context, response: CurrencyRateResponse): List<Currency> {

    val jsonString = readRawResource(context, R.raw.currency_dictionary)
    val jsonArray = JsonParser.parseString(jsonString).asJsonArray

    // Primero, generamos el diccionario de monedas a partir del JSON
    val currencyDictionary = jsonArray.mapNotNull { element ->
        val currencyInfo = element.asJsonObject

        val currencyIsoCode = currencyInfo.get("iso_code")?.asString
        val currencyName = currencyInfo.get("name")?.asString
        val countryName = currencyInfo.get("country")?.asString
        val currencySymbol = currencyInfo.get("symbol")?.asString

        if (
            currencyIsoCode != null &&
            currencyName != null &&
            countryName != null &&
            currencySymbol != null
        ) {
            Currency(
                isoCode = currencyIsoCode,
                countryName = countryName,
                currencyName = currencyName,
                currencySymbol = currencySymbol,
                exchangeRate = 0.0 // Este valor será reemplazado más tarde
            )
        } else {
            null
        }
    }

    // Luego, mapeamos las tasas de cambio a las monedas correspondientes en el diccionario
    return response.rates.mapNotNull { (isoCode, exchangeRate) ->
        val currency = currencyDictionary.find { it.isoCode == isoCode }

        // Si encontramos la moneda en el diccionario, actualizamos su tasa de cambio
        currency?.apply { this.exchangeRate = exchangeRate }
    }
}
