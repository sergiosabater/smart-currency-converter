package com.sergiosabater.smartcurrencyconverter.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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

    // Recolectamos los StateFlow del ViewModel como un State en Compose
    val displayText by mainViewModel.displayText.collectAsState()
    val displaySymbol by mainViewModel.displaySymbol.collectAsState()
    val conversionResult by mainViewModel.conversionResult.collectAsState()
    val conversionSymbol by mainViewModel.conversionSymbol.collectAsState()
    val currencies by mainViewModel.currencies.collectAsState()
    val defaultCurrency1 by mainViewModel.selectedCurrency1.collectAsState()
    val defaultCurrency2 by mainViewModel.selectedCurrency2.collectAsState()

    val mDisplay = Display()
    val mCurrencySelector = CurrencySelector()
    val mKeyboard = Keyboard()
    val keyboardConfig = KeyboardConfig()

    // Comprueba que las monedas se hayan cargado antes de intentar mostrarlas
    if (currencies.isEmpty()) {
        // Muestra un spinner de carga o un mensaje de error
        CircularProgressIndicator()
    } else {
        Column {
            // Primer display
            mDisplay.CustomDisplay(displayText = displayText, symbol = displaySymbol)

            // Divider crea una línea horizontal entre los dos displays
            Divider(color = Color.Gray, thickness = 2.dp)

            // Segundo display
            mDisplay.CustomDisplay(
                displayText = conversionResult,
                symbol = conversionSymbol
            )

            // Selector de monedas
            mCurrencySelector.CustomCurrencySelector(
                currencies,
                mainViewModel::onCurrencySelected,
                defaultCurrency1 ?: currencies[0],
                defaultCurrency2 ?: currencies[0]
            )

            //Teclado
            mKeyboard.CustomKeyboard(
                config = keyboardConfig,
                onClearButtonClick = mainViewModel::onClearButtonClicked,
                onNumericButtonClicked = mainViewModel::onNumericButtonClicked,
                onBackspaceClicked = mainViewModel::onBackspaceClicked
            )
        }
    }
}
