package com.sergiosabater.smartcurrencyconverter.repository

import com.sergiosabater.smartcurrencyconverter.data.network.ApiResult
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse

interface CurrencyRepository {
    suspend fun getCurrencyRates(): ApiResult<CurrencyRateResponse>
}