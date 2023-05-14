package com.sergiosabater.smartcurrencyconverter.usecase.keyboard


import com.sergiosabater.smartcurrencyconverter.util.format.formatDisplay

/**
 * Caso de uso que maneja la acción del botón de retroceso en la aplicación.
 */
class HandleBackspaceUseCase {

    fun execute(input: String): String {
        val updatedInput = removeLastCharacter(input)
        return formatDisplay(updatedInput)
    }

    /**
     *
     * Elimina el último carácter de una cadena dada. Si la cadena es vacía o contiene
     * solo "0" o tiene una longitud de 1, se devuelve "0".
     * Si el penúltimo carácter es una coma (",") también se elimina.
     * @param input La cadena de entrada a procesar.
     * @return La cadena procesada con el último carácter eliminado.
     */
    private fun removeLastCharacter(input: String): String {
        // Si el input es vacío, solo hay un "0" o solo hay un caracter en el input, devolvemos "0"
        if (input.isEmpty() || input == "0" || input.length == 1) {
            return "0"
        }

        // Si hay más de un caracter, eliminamos el último y devolvemos el resultado
        var updatedInput = input.substring(0, input.length - 1)

        // Si el último caracter es ",", eliminamos ese caracter también
        if (updatedInput.lastOrNull() == ',') {
            updatedInput = updatedInput.substring(0, updatedInput.length - 1)
        }

        return updatedInput
    }

}