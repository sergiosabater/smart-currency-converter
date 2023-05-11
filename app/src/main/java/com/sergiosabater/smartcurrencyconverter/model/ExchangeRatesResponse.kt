package com.sergiosabater.smartcurrencyconverter.model

data class ExchangeRatesResponse(
    val base: String,
    val rates: Map<String, Float>,
    val timestamp: Long
)
