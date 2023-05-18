package com.sergiosabater.smartcurrencyconverter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


// Entidad que representa la tabla en la base de datos donde almacenaremos las tasas de cambio de moneda.
@Entity(tableName = "currency_rates")
data class CurrencyRateEntity(
    @PrimaryKey @ColumnInfo(name = "base") val base: String,
    @ColumnInfo(name = "rates") val rates: Map<String, Double>,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "disclaimer") val disclaimer: String,
    @ColumnInfo(name = "license") val license: String
)
