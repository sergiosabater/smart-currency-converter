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
import com.sergiosabater.smartcurrencyconverter.ui.components.config.KeyboardConfig
import com.sergiosabater.smartcurrencyconverter.util.constant.SymbolConstants.BACKSPACE_SYMBOL_STRING
import com.sergiosabater.smartcurrencyconverter.util.constant.SymbolConstants.SETTINGS_SYMBOL_STRING
import com.sergiosabater.smartcurrencyconverter.util.constant.TextConstants.CLEAR_BUTTON_STRING
import com.sergiosabater.smartcurrencyconverter.util.constant.TextConstants.COMMA_STRING

class Keyboard {

    @Composable
    fun CustomKeyboard(
        config: KeyboardConfig,
        onClearButtonClick: () -> Unit,
        onNumericButtonClicked: (String) -> Unit,
        onBackspaceClicked: () -> Unit,
        onSettingsButtonClicked: () -> Unit,
        onKeyClicked: (String) -> Unit
    ) {
        // BoxWithConstraints to get the available screen dimensions
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            // Calculate the button size based on the minimum of maxWidth and maxHeight
            val buttonSize = minOf(maxWidth, maxHeight) / 4
            // Store the screen width in a mutable state to be used later
            val screenWidth = remember { mutableStateOf(maxWidth) }

            Column(modifier = Modifier.fillMaxWidth()) {
                for (i in config.buttonSymbols.indices) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (j in config.buttonSymbols[i].indices) {
                            val colors =
                                Pair(config.buttonColors[i][j], config.buttonTextColors[i][j])

                            Surface(color = Color.Gray) {
                                TextButton(
                                    onClick = {
                                        onKeyClicked(config.buttonSymbols[i][j])
                                        when {
                                            config.buttonSymbols[i][j] == CLEAR_BUTTON_STRING -> {
                                                onClearButtonClick()
                                            }

                                            config.buttonSymbols[i][j] == BACKSPACE_SYMBOL_STRING -> {
                                                onBackspaceClicked()
                                            }

                                            config.buttonSymbols[i][j] == SETTINGS_SYMBOL_STRING -> {
                                                onSettingsButtonClicked()
                                            }

                                            config.buttonSymbols[i][j].first()
                                                .isDigit() || config.buttonSymbols[i][j] == COMMA_STRING -> {
                                                onNumericButtonClicked(config.buttonSymbols[i][j])
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .width(screenWidth.value / config.buttonSymbols[i].size)
                                        .height(buttonSize)
                                        .padding(1.dp),
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = colors.first,
                                        contentColor = colors.second
                                    ),
                                    shape = RectangleShape
                                ) {
                                    Text(
                                        text = config.buttonSymbols[i][j],
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