package com.sergiosabater.smartcurrencyconverter.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.lifecycle.viewmodel.compose.viewModel
import com.sergiosabater.smartcurrencyconverter.ui.components.Display
import com.sergiosabater.smartcurrencyconverter.ui.components.MyKeyboard
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
        val myDisplay = Display()
        val myKeyboard = MyKeyboard()
        myDisplay.CustomDisplay(displayText = displayText)
        myKeyboard.CustomKeyboard(
            onClearButtonClick = mainViewModel::onClearButtonClicked,
            onNumericButtonClicked = mainViewModel::onNumericButtonClicked
        )
    }
}