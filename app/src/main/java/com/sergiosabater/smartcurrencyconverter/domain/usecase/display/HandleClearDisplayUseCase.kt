package com.sergiosabater.smartcurrencyconverter.domain.usecase.display

/**
 * Caso de uso que maneja la acción de limpiar el display en la aplicación.
 */
class HandleClearDisplayUseCase {

    /**
     * Limpia el display, restableciéndolo a su valor inicial de "0".
     *
     * @return Una cadena de texto que representa el valor inicial del display ("0").
     */
    fun execute(): String {
        return "0"
    }
}
