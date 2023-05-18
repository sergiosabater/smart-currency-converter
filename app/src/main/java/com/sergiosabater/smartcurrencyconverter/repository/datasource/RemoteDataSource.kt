package com.sergiosabater.smartcurrencyconverter.repository.datasource

import com.sergiosabater.smartcurrencyconverter.data.network.ApiInterface
import com.sergiosabater.smartcurrencyconverter.data.network.ApiResult
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse

class RemoteDataSource(private val apiInterface: ApiInterface) {
    suspend fun getCurrencyRates(appId: String): ApiResult<CurrencyRateResponse> {
        return try {
            val response = apiInterface.getCurrencyRates(appId)
            if (response.isSuccessful) {
                response.body()?.let { ApiResult.Success(it) }
                    ?: ApiResult.Error(Exception("No data found"))
            } else {
                ApiResult.Error(Exception("Error ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}