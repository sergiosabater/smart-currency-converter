package com.sergiosabater.smartcurrencyconverter.util.parser

import android.content.Context
import com.sergiosabater.smartcurrencyconverter.domain.model.Currency
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse

interface CurrencyApiHelper {
    fun loadCurrenciesFromApi(context: Context, response: CurrencyRateResponse): List<Currency>
}