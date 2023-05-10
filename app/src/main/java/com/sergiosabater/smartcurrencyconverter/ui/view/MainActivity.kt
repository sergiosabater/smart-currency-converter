package com.sergiosabater.smartcurrencyconverter.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
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

    Column {
        val mDisplay = Display()
        val mCurrencySelector = CurrencySelector()
        val mKeyboard = Keyboard()
        val keyboardConfig = KeyboardConfig()
        mDisplay.CustomDisplay(displayText = displayText)
        mCurrencySelector.CustomCurrencySelector()
        mKeyboard.CustomKeyboard(
            config = keyboardConfig,
            onClearButtonClick = mainViewModel::onClearButtonClicked,
            onNumericButtonClicked = mainViewModel::onNumericButtonClicked,
            onBackspaceClicked = mainViewModel::onBackspaceClicked
        )
    }
}