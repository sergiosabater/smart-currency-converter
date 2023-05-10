package com.sergiosabater.smartcurrencyconverter.ui.components.config

import androidx.compose.ui.graphics.Color
import com.sergiosabater.smartcurrencyconverter.util.constant.SymbolConstants.BACKSPACE_SYMBOL_STRING
import com.sergiosabater.smartcurrencyconverter.util.constant.SymbolConstants.CONVERSION_SYMBOL_STRING
import com.sergiosabater.smartcurrencyconverter.util.constant.TextConstants.CLEAR_BUTTON_STRING

data class KeyboardConfig(
    val buttonColors: List<List<Color>> = defaultButtonColors(),
    val buttonTextColors: List<List<Color>> = defaultButtonTextColors(),
    val buttonSymbols: List<List<String>> = defaultButtonSymbols()
)

fun defaultButtonColors() = listOf(
    listOf(Color.Red, Color.DarkGray, Color.DarkGray),
    listOf(Color.White, Color.White, Color.White),
    listOf(Color.White, Color.White, Color.White),
    listOf(Color.White, Color.White, Color.White),
    listOf(Color.White, Color.White, Color.White)
)

fun defaultButtonTextColors() = listOf(
    listOf(Color.White, Color.White, Color.White),
    listOf(Color.Black, Color.Black, Color.Black),
    listOf(Color.Black, Color.Black, Color.Black),
    listOf(Color.Black, Color.Black, Color.Black),
    listOf(Color.Black, Color.Black, Color.Black)
)

fun defaultButtonSymbols() = listOf(
    listOf(CLEAR_BUTTON_STRING, BACKSPACE_SYMBOL_STRING, CONVERSION_SYMBOL_STRING),
    listOf("7", "8", "9"),
    listOf("4", "5", "6"),
    listOf("1", "2", "3"),
    listOf("⚙", "0", ",")
)
