package com.sergiosabater.smartcurrencyconverter.repository


import com.sergiosabater.smartcurrencyconverter.data.network.ApiInterface
import com.sergiosabater.smartcurrencyconverter.data.network.ApiResult
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse
import com.sergiosabater.smartcurrencyconverter.util.constant.Config.API_KEY

class CurrencyRepositoryImpl(private val apiInterface: ApiInterface) : CurrencyRepository {
    override suspend fun getCurrencyRates(): ApiResult<CurrencyRateResponse> {
        return try {
            val response = apiInterface.getCurrencyRates(API_KEY)
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