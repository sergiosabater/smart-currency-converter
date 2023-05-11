package com.sergiosabater.smartcurrencyconverter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Display {

    @Composable
    fun CustomDisplay(displayText: String, symbol: String) {

        // Estilos de texto reutilizables
        val textStyle = TextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 60.sp,
            color = Color.Black
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(5.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            // La propiedad horizontalScroll permite scroll horizontal del display
            Box(
                modifier = Modifier
                    .padding(end = 10.dp)  // Agrega padding a la derecha
                    .horizontalScroll(rememberScrollState())
            ) {
                // Formato de fuente del display
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Texto del display
                    Text(
                        text = displayText,
                        style = textStyle,
                    )

                    // Símbolo de la moneda
                    Text(
                        text = " $symbol",
                        style = textStyle,
                    )
                }
            }
        }
    }
}