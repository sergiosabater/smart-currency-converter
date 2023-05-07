package com.sergiosabater.smartcurrencyconverter.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.sergiosabater.smartcurrencyconverter.ui.components.MyKeyboard
import com.sergiosabater.smartcurrencyconverter.ui.theme.SmartCurrencyConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCurrencyConverterTheme {
                MyKeyboardContent()
            }
        }
    }
}

@Composable
fun MyKeyboardContent() {
    val myKeyboard = MyKeyboard()
    myKeyboard.CustomKeyboard()
}