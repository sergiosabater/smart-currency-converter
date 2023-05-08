package com.sergiosabater.smartcurrencyconverter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sergiosabater.smartcurrencyconverter.utils.constants.SymbolConstants.BACKSPACE_SYMBOL
import com.sergiosabater.smartcurrencyconverter.utils.constants.SymbolConstants.CONVERSION_SYMBOL

class MyKeyboard {

    @Composable
    fun CustomKeyboard(
        onClearButtonClick: () -> Unit,
        onNumericButtonClicked: (String) -> Unit,
        onBackspaceClicked: () -> Unit
    ) {
        // BoxWithConstraints to get the available screen dimensions
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            // Calculate the button size based on the minimum of maxWidth and maxHeight
            val buttonSize = minOf(maxWidth, maxHeight) / 4
            // Store the screen width in a mutable state to be used later
            val screenWidth = remember { mutableStateOf(maxWidth) }

            val buttonColors = listOf(
                listOf(Color.Red, Color.DarkGray, Color.DarkGray),
                listOf(Color.White, Color.White, Color.White),
                listOf(Color.White, Color.White, Color.White),
                listOf(Color.White, Color.White, Color.White),
                listOf(Color.White, Color.White, Color.White)
            )

            val buttonTextColors = listOf(
                listOf(Color.White, Color.White, Color.White),
                listOf(Color.Black, Color.Black, Color.Black),
                listOf(Color.Black, Color.Black, Color.Black),
                listOf(Color.Black, Color.Black, Color.Black),
                listOf(Color.Black, Color.Black, Color.Black)
            )

            val buttonSymbols = listOf(
                listOf("C", BACKSPACE_SYMBOL, CONVERSION_SYMBOL),
                listOf("7", "8", "9"),
                listOf("4", "5", "6"),
                listOf("1", "2", "3"),
                listOf("⚙", "0", ",")
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                for (i in buttonSymbols.indices) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (j in buttonSymbols[i].indices) {
                            val colors = Pair(buttonColors[i][j], buttonTextColors[i][j])

                            Surface(color = Color.Gray) {
                                TextButton(
                                    onClick = {
                                        when {
                                            buttonSymbols[i][j] == "C" -> {
                                                onClearButtonClick()
                                            }

                                            buttonSymbols[i][j] == BACKSPACE_SYMBOL -> {
                                                onBackspaceClicked()
                                            }

                                            buttonSymbols[i][j].first()
                                                .isDigit() || buttonSymbols[i][j] == "," -> {
                                                onNumericButtonClicked(buttonSymbols[i][j])
                                            }

                                            else -> {
                                                //onButtonClicked(buttonSymbols[i][j])
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .width(screenWidth.value / buttonSymbols[i].size)
                                        .height(buttonSize)
                                        .padding(1.dp),
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = colors.first,
                                        contentColor = colors.second
                                    ),
                                    shape = RectangleShape
                                ) {
                                    Text(
                                        text = buttonSymbols[i][j],
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Light
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