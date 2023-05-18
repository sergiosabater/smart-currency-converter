package com.sergiosabater.smartcurrencyconverter.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyRateDao {

    @Query("SELECT * FROM currency_rates WHERE base = :base")
    suspend fun getCurrencyRates(base: String): CurrencyRateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyRates(currencyRate: CurrencyRateEntity)

    @Query("DELETE FROM currency_rates")
    suspend fun deleteAllCurrencyRates()
}