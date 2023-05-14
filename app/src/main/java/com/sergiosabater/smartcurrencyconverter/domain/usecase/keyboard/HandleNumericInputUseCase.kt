package com.sergiosabater.smartcurrencyconverter.domain.usecase.keyboard

import com.sergiosabater.smartcurrencyconverter.util.format.formatDisplay
import com.sergiosabater.smartcurrencyconverter.util.format.updateDisplay


/**
 * Caso de uso que se encarga de manejar la entrada numérica en la aplicación.
 */
class HandleNumericInputUseCase {

    /**
     * Actualiza el valor actualmente mostrado y lo formatea.
     *
     * @param currentDisplay La cadena de texto actualmente mostrada.
     * @param digit El dígito numérico que se va a añadir a la cadena desplegada.
     * @return Una cadena de texto que representa el valor actualizado y formateado.
     */
    fun execute(currentDisplay: String, digit: String): String {
        val updatedDisplay = updateDisplay(currentDisplay, digit)
        return formatDisplay(updatedDisplay)
    }
}
