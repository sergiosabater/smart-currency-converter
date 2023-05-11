package com.sergiosabater.smartcurrencyconverter.model

data class Currency(
    val isoCode: String,
    val countryName: String,
    val currencyName: String,
    val currencySymbol: String,
    val exchangeRate: Double
)