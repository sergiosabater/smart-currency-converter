package com.sergiosabater.smartcurrencyconverter.viewmodel

import com.sergiosabater.smartcurrencyconverter.domain.model.Currency
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse


object TestHelpers {

    // Datos de prueba
    val ratesData = mapOf(
        "USD" to 1.0,
        "EUR" to 0.85,
        "GBP" to 0.75
    )

    val response = CurrencyRateResponse(
        disclaimer = "Disclaimer test",
        license = "License test",
        timestamp = 123456789L,
        base = "USD",
        rates = ratesData
    )

    val jsonString = """[
  {"iso_code": "USD", "name": "United States Dollar", "country": "United States", "symbol": "$"},
  {"iso_code": "EUR", "name": "Euro", "country": "European Union", "symbol": "€"},
  {"iso_code": "GBP", "name": "British Pound", "country": "United Kingdom", "symbol": "£"}
  ]""".trimIndent()

    fun generateCurrencyList(): List<Currency> {

        // Definición manual de las monedas que queremos
        return listOf(
            Currency("USD", "United States", "United States Dollar", "$", 1.0),
            Currency("EUR", "European Union", "Euro", "€", 0.85),
            Currency("GBP", "United Kingdom", "British Pound", "£", 0.75)
        )
    }
}
