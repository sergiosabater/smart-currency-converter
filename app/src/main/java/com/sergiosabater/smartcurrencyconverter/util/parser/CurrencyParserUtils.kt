package com.sergiosabater.smartcurrencyconverter.util.parser

import android.content.Context
import com.google.gson.JsonParser
import com.sergiosabater.smartcurrencyconverter.model.Currency
import java.io.BufferedReader
import java.io.InputStreamReader

fun parseCurrencies(context: Context, rawResourceId: Int): List<Currency> {
    val jsonString = readRawResource(context, rawResourceId)

    val jsonElement = JsonParser.parseString(jsonString)
    val jsonObject = jsonElement.asJsonObject

    return jsonObject.entrySet().mapNotNull { entry ->
        val currencyIsoCode = entry.key
        val currencyInfo = entry.value.asJsonObject

        val currencyName = currencyInfo.get("currency_name")?.asString
        val countryName = currencyInfo.get("country")?.asString

        if (currencyName != null && countryName != null) {
            Currency(
                isoCode = currencyIsoCode,
                countryName = countryName,
                currencyName = currencyName,
                conversionRate = 1.0
            )
        } else {
            null
        }
    }
}

private fun readRawResource(context: Context, rawResourceId: Int): String {
    val inputStream = context.resources.openRawResource(rawResourceId)
    val reader = BufferedReader(InputStreamReader(inputStream))
    return reader.use { it.readText() }
}