package com.sergiosabater.smartcurrencyconverter.repository


import com.sergiosabater.smartcurrencyconverter.data.network.ApiInterface
import com.sergiosabater.smartcurrencyconverter.data.network.ApiResult
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse
import com.sergiosabater.smartcurrencyconverter.repository.datasource.LocalDataSource
import com.sergiosabater.smartcurrencyconverter.util.constant.Config.API_KEY

class CurrencyRepositoryImpl(
    private val apiInterface: ApiInterface,
    private val localDataSource: LocalDataSource
) : CurrencyRepository {

    override suspend fun getCurrencyRates(): ApiResult<CurrencyRateResponse> {
        return try {
            val response = apiInterface.getCurrencyRates(API_KEY)
            if (response.isSuccessful) {
                response.body()?.let {
                    // Si la respuesta de la API es exitosa, guarda la información en la base de datos local
                    localDataSource.saveCurrencyRates(it)
                    ApiResult.Success(it)
                } ?: ApiResult.Error(Exception("No data found"))
            } else {
                // Si hay un error en la respuesta de la API, intenta obtener los datos de la base de datos local
                val localData = localDataSource.getCurrencyRates()
                localData?.let {
                    ApiResult.Success(it)
                } ?: ApiResult.Error(Exception("Error ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            // Si hay un error en la solicitud de la API, intenta obtener los datos de la base de datos local
            val localData = localDataSource.getCurrencyRates()
            localData?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Error(e)
        }
    }
}