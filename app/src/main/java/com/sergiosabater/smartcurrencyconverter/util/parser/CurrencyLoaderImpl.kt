package com.sergiosabater.smartcurrencyconverter.util.parser

import android.content.Context
import com.google.gson.JsonParser
import com.sergiosabater.smartcurrencyconverter.R
import com.sergiosabater.smartcurrencyconverter.domain.model.Currency
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse
import java.io.BufferedReader
import java.io.InputStreamReader

class CurrencyLoaderImpl(private val context: Context) : CurrencyLoader {
    override fun loadCurrenciesFromApi(response: CurrencyRateResponse): List<Currency> {
        // Implementa aquí la lógica para cargar las monedas
        // Esto puede incluir el uso de 'context' para acceder a los recursos del sistema
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

    private fun readRawResource(context: Context, rawResourceId: Int): String {
        val inputStream = context.resources.openRawResource(rawResourceId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }
}