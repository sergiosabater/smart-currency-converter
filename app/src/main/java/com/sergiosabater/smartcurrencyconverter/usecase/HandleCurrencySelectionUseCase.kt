package com.sergiosabater.smartcurrencyconverter.usecase

import com.sergiosabater.smartcurrencyconverter.model.Currency

/**
 * Caso de uso para manejar la selección de monedas.
 * Encuentra el símbolo de la moneda basado en el nombre de una moneda seleccionada.
 *
 * @param currencies Una lista de objetos [Currency] para buscar.
 * @param selectedCurrencyName El nombre de la moneda seleccionada.
 * @return El símbolo de la moneda seleccionada, o " " si no se encuentra ninguna coincidencia.
 */

class HandleCurrencySelectionUseCase {
    fun execute(currencies: List<Currency>, selectedCurrencyName: String): String {
        val selectedCurrency = currencies.find { it.currencyName == selectedCurrencyName }
        return selectedCurrency?.currencySymbol ?: " " // Cadena vacía si no encuentra valor
    }
}