package com.sergiosabater.smartcurrencyconverter.data

import com.sergiosabater.smartcurrencyconverter.model.Currency
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiInterface {
    @GET
    suspend fun getCurrencies(@Url url: String): Currency
}