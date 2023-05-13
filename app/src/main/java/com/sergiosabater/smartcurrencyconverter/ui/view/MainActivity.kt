package com.sergiosabater.smartcurrencyconverter.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.lifecycle.viewmodel.compose.viewModel
import com.sergiosabater.smartcurrencyconverter.ui.components.CurrencySelector
import com.sergiosabater.smartcurrencyconverter.ui.components.Display
import com.sergiosabater.smartcurrencyconverter.ui.components.Keyboard
import com.sergiosabater.smartcurrencyconverter.ui.components.config.KeyboardConfig
import com.sergiosabater.smartcurrencyconverter.ui.theme.SmartCurrencyConverterTheme
import com.sergiosabater.smartcurrencyconverter.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCurrencyConverterTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val mainViewModel: MainViewModel = viewModel()
    val displayText by mainViewModel.displayText.collectAsState()
    val displaySymbol by mainViewModel.displaySymbol.collectAsState()
    val currencies by mainViewModel.currencies.collectAsState()
    val selectedCurrency1 by mainViewModel.selectedCurrency1.collectAsState()
    val selectedCurrency2 by mainViewModel.selectedCurrency2.collectAsState()

    val mDisplay = Display()
    val mCurrencySelector = CurrencySelector()
    val mKeyboard = Keyboard()
    val keyboardConfig = KeyboardConfig()

    // Comprueba que las monedas se hayan cargado antes de intentar mostrarlas
    if (currencies.isEmpty()) {
        // Muestra un spinner de carga o un mensaje de error
        CircularProgressIndicator()
    } else {
        val defaultCurrency1 = currencies.find { it.isoCode == "EUR" } ?: currencies[0]
        val defaultCurrency2 = currencies.find { it.isoCode == "USD" } ?: currencies[0]

        Column {
            mDisplay.CustomDisplay(displayText = displayText, symbol = displaySymbol)
            mCurrencySelector.CustomCurrencySelector(
                currencies,
                mainViewModel::onCurrencySelected,
                defaultCurrency1,
                defaultCurrency2
            )
            mKeyboard.CustomKeyboard(
                config = keyboardConfig,
                onClearButtonClick = mainViewModel::onClearButtonClicked,
                onNumericButtonClicked = mainViewModel::onNumericButtonClicked,
                onBackspaceClicked = mainViewModel::onBackspaceClicked,
                onConversionButtonClicked = {
                    if (selectedCurrency1 != null && selectedCurrency2 != null) {
                        mainViewModel.onConversionButtonClicked()
                    }
                }
            )
        }
    }
}