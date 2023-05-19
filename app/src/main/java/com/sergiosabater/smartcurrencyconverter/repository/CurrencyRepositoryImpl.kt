package com.sergiosabater.smartcurrencyconverter.repository


import com.sergiosabater.smartcurrencyconverter.data.network.ApiInterface
import com.sergiosabater.smartcurrencyconverter.data.network.ApiResult
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse
import com.sergiosabater.smartcurrencyconverter.repository.datasource.LocalDataSource
import com.sergiosabater.smartcurrencyconverter.repository.datasource.RemoteDataSource
import com.sergiosabater.smartcurrencyconverter.util.constant.Config.API_KEY

class CurrencyRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : CurrencyRepository {

    override suspend fun getCurrencyRates(): ApiResult<CurrencyRateResponse> {
        return try {
            when (val response = remoteDataSource.getCurrencyRates(API_KEY)) {
                is ApiResult.Success -> {
                    // Si la respuesta de la API es exitosa, guarda la información en la base de datos local
                    localDataSource.saveCurrencyRates(response.data)
                    ApiResult.Success(response.data)
                }
                is ApiResult.Error -> {
                    // Si hay un error en la respuesta de la API, intenta obtener los datos de la base de datos local
                    getLocalData(response.exception)
                }
            }
        } catch (e: Exception) {
            // Si hay un error en la solicitud de la API, intenta obtener los datos de la base de datos local
            getLocalData(e)
        }
    }

    private suspend fun getLocalData(exception: Exception): ApiResult<CurrencyRateResponse> {
        val localData = localDataSource.getCurrencyRates()
        return localData?.let {
            ApiResult.Success(it)
        } ?: ApiResult.Error(exception)
    }
}


