package com.sergiosabater.smartcurrencyconverter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MyKeyboard {

    @Composable
    fun CustomKeyboard(onClearButtonClick: () -> Unit, onNumericButtonClicked: (String) -> Unit) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val buttonSize = minOf(maxWidth, maxHeight) / 4

            val buttonColors = listOf(
                listOf(Color.Red, Color.DarkGray, Color.DarkGray, Color.DarkGray),
                listOf(Color.White, Color.White, Color.White, Color.DarkGray),
                listOf(Color.White, Color.White, Color.White, Color.DarkGray),
                listOf(Color.White, Color.White, Color.White, Color.DarkGray),
                listOf(Color.White, Color.White, Color.White, Color.Green)
            )

            val buttonTextColors = listOf(
                listOf(Color.White, Color.White, Color.White, Color.White),
                listOf(Color.Black, Color.Black, Color.Black, Color.White),
                listOf(Color.Black, Color.Black, Color.Black, Color.White),
                listOf(Color.Black, Color.Black, Color.Black, Color.White),
                listOf(Color.Black, Color.Black, Color.Black, Color.White)
            )

            val buttonSymbols = listOf(
                listOf("C", "←", "%", "÷"),
                listOf("7", "8", "9", "×"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf("⚙", "0", ",", "=")
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                for (i in 0 until 5) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (j in 0 until 4) {
                            val colors = Pair(buttonColors[i][j], buttonTextColors[i][j])

                            Surface(color = Color.Gray) {
                                TextButton(
                                    onClick = {
                                        when {
                                            buttonSymbols[i][j] == "C" -> {
                                                onClearButtonClick()
                                            }
                                            buttonSymbols[i][j].first().isDigit() || buttonSymbols[i][j] == "," -> {
                                                onNumericButtonClicked(buttonSymbols[i][j])
                                            }
                                            else -> {
                                                //onButtonClicked(buttonSymbols[i][j])
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .size(buttonSize)
                                        .padding(1.dp),
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = colors.first,
                                        contentColor = colors.second
                                    ),
                                    shape = RectangleShape
                                ) {
                                    Text(
                                        text = buttonSymbols[i][j],
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}