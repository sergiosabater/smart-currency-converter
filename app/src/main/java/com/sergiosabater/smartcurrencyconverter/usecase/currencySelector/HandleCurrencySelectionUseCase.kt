package com.sergiosabater.smartcurrencyconverter.usecase.currencySelector

import com.sergiosabater.smartcurrencyconverter.model.Currency


/**
 * Caso de uso que se encarga de manejar la acción de seleccionar una moneda
 * en la aplicación.
 */
class HandleCurrencySelectionUseCase {

    /**
     * Devuelve el símbolo de la moneda seleccionada.
     *
     * @param currency La moneda seleccionada.
     * @return Una cadena de texto que representa el símbolo de la moneda seleccionada.
     */
    fun execute(currency: Currency): String {
        return currency.currencySymbol
    }
}
