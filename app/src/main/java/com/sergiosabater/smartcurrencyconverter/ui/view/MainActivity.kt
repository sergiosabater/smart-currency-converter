package com.sergiosabater.smartcurrencyconverter.ui.view

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController

import com.sergiosabater.smartcurrencyconverter.data.network.RetrofitClient
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyResult
import com.sergiosabater.smartcurrencyconverter.domain.usecase.common.NavigateToSettingsUseCase
import com.sergiosabater.smartcurrencyconverter.repository.CurrencyRepository
import com.sergiosabater.smartcurrencyconverter.repository.CurrencyRepositoryImpl
import com.sergiosabater.smartcurrencyconverter.ui.components.CurrencySelector
import com.sergiosabater.smartcurrencyconverter.ui.components.Display
import com.sergiosabater.smartcurrencyconverter.ui.components.Keyboard
import com.sergiosabater.smartcurrencyconverter.ui.components.SplashScreen
import com.sergiosabater.smartcurrencyconverter.ui.components.config.KeyboardConfig
import com.sergiosabater.smartcurrencyconverter.ui.theme.SmartCurrencyConverterTheme
import com.sergiosabater.smartcurrencyconverter.viewmodel.MainViewModel
import com.sergiosabater.smartcurrencyconverter.viewmodel.MainViewModelFactory
import com.sergiosabater.smartcurrencyconverter.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiInterface = RetrofitClient.instance
        val currencyRepository = CurrencyRepositoryImpl(apiInterface)
        setContent {
            SmartCurrencyConverterTheme {
                val navController = rememberNavController()
                val settingsViewModel: SettingsViewModel = viewModel()

                NavHost(navController, startDestination = "main") {
                    composable("main") { MainScreen(currencyRepository, navController) }
                    composable("settings") {
                        SettingsScreen(
                            settingsViewModel = settingsViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(currencyRepository: CurrencyRepository, navController: NavController) {
    val navigateToSettingsUseCase = NavigateToSettingsUseCase(navController)
    val mainViewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(
            LocalContext.current.applicationContext as Application,
            currencyRepository,
            navigateToSettingsUseCase
        )
    )

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
    val splashScreen = SplashScreen()

    
    when (currencies) {

        is CurrencyResult.Loading -> {
            splashScreen.Splash()
        }

        is CurrencyResult.Success -> {
            val currencies = (currencies as CurrencyResult.Success).data
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
                    onBackspaceClicked = mainViewModel::onBackspaceClicked,
                    onSettingsButtonClicked = mainViewModel::onSettingsButtonClicked,
                    onKeyClicked = mainViewModel::onKeyClicked
                )
            }
        }

        is CurrencyResult.Failure -> {
            // Muestra un Composable para indicar un error

        }
    }
}

