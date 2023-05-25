package com.sergiosabater.smartcurrencyconverter.util.parser

import com.sergiosabater.smartcurrencyconverter.domain.model.Currency
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse

interface CurrencyLoader {
    fun loadCurrenciesFromApi(response: CurrencyRateResponse): List<Currency>
}