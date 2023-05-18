package com.sergiosabater.smartcurrencyconverter.repository.datasource

import com.sergiosabater.smartcurrencyconverter.database.CurrencyRateDao
import com.sergiosabater.smartcurrencyconverter.database.CurrencyRateEntity
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse

class LocalDataSource(private val currencyRateDao: CurrencyRateDao) {
    suspend fun getCurrencyRates(): CurrencyRateResponse? {
        val entity = currencyRateDao.getCurrencyRates("USD")
        return entity?.let { entityToCurrencyRateResponse(it) }
    }

    suspend fun saveCurrencyRates(currencyRateResponse: CurrencyRateResponse) {
        val entity = currencyRateResponseToEntity(currencyRateResponse)
        currencyRateDao.insertCurrencyRates(entity)
    }


    private fun entityToCurrencyRateResponse(entity: CurrencyRateEntity): CurrencyRateResponse {
        return CurrencyRateResponse(
            disclaimer = entity.disclaimer,
            license = entity.license,
            timestamp = entity.timestamp,
            base = entity.base,
            rates = entity.rates
        )
    }

    private fun currencyRateResponseToEntity(response: CurrencyRateResponse): CurrencyRateEntity {
        return CurrencyRateEntity(
            disclaimer = response.disclaimer,
            license = response.license,
            timestamp = response.timestamp,
            base = response.base,
            rates = response.rates
        )
    }
}