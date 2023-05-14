package com.sergiosabater.smartcurrencyconverter.data.network

import com.sergiosabater.smartcurrencyconverter.domain.model.Currency
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {
    @GET("latest.json")
    suspend fun getCurrencyRates(@Query("app_id") appId: String): Response<CurrencyRateResponse>
}