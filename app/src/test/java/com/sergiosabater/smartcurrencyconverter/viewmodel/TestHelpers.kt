package com.sergiosabater.smartcurrencyconverter.viewmodel

import com.sergiosabater.smartcurrencyconverter.domain.model.Currency
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse


object TestHelpers {

    fun loadCurrenciesFromApi(response: CurrencyRateResponse): List<Currency> {

        // Definición manual de las monedas que queremos en nuestro diccionario
        val currencyDictionary = listOf(
            Currency("USD", "United States", "United States Dollar", "$", 0.0),
            Currency("EUR", "European Union", "Euro", "€", 0.0),
            Currency("GBP", "United Kingdom", "British Pound", "£", 0.0)
        )

        // Luego, mapeamos las tasas de cambio a las monedas correspondientes en el diccionario
        return response.rates.mapNotNull { (isoCode, exchangeRate) ->
            val currency = currencyDictionary.find { it.isoCode == isoCode }

            // Si encontramos la moneda en el diccionario, actualizamos su tasa de cambio
            currency?.apply { this.exchangeRate = exchangeRate }
        }
    }

}
