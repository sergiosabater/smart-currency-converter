package com.sergiosabater.smartcurrencyconverter.domain.model

sealed class CurrencyResult {
    object Loading : CurrencyResult()
    data class Success(val data: List<Currency>) : CurrencyResult()
    data class Failure(val error: Throwable) : CurrencyResult()
}