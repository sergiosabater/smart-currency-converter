package com.sergiosabater.smartcurrencyconverter.viewmodel

import android.app.Application
import com.sergiosabater.smartcurrencyconverter.data.network.ApiResult
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyRateResponse
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyResult
import com.sergiosabater.smartcurrencyconverter.domain.usecase.common.NavigateToSettingsUseCase
import com.sergiosabater.smartcurrencyconverter.repository.CurrencyRepository
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    // Dependiencias mockeadas
    private val mockCurrencyRepository = mockk<CurrencyRepository>()
    private val mockNavigateToSettingsUseCase = mockk<NavigateToSettingsUseCase>()
    private val application = mockk<Application>()

    // Crear el ViewModel con las dependencias mockeadas
    private val viewModel = MainViewModel(application, mockCurrencyRepository, mockNavigateToSettingsUseCase)

    // Preparar la respuesta de la API
    private val ratesData = mapOf(
        "USD" to 1.0,
        "EUR" to 0.85,
        "GBP" to 0.75
    )

    private val response = CurrencyRateResponse(
        disclaimer = "Disclaimer test",
        license = "License test",
        timestamp = 123456789L,
        base = "USD",
        rates = ratesData
    )

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {
    }

    @Test
    fun loadCurrencies() {

        // Configurar las respuestas de las dependencias mockeadas
        coEvery { mockCurrencyRepository.getCurrencyRates() } returns ApiResult.Success(response)

        // Creamos el resultado esperado
        val expectedResult = CurrencyResult.Success(TestHelpers.loadCurrenciesFromApi(response))

        // Llamar al método a testear
        viewModel.loadCurrencies()

        // Los resultados obtenidos
        val result = viewModel.currencies.value

        // Aquí verificamos que 'result' contiene los datos esperados.
        assertEquals(expectedResult, result)
    }

    @Test
    fun getCurrencies() {
    }

    @Test
    fun getDisplayText() {
    }

    @Test
    fun getDisplaySymbol() {
    }

    @Test
    fun getSelectedCurrency1() {
    }

    @Test
    fun getSelectedCurrency2() {
    }

    @Test
    fun getConversionResult() {
    }

    @Test
    fun getConversionSymbol() {
    }

    @Test
    fun onClearButtonClicked() {
    }

    @Test
    fun onNumericButtonClicked() {
    }

    @Test
    fun onBackspaceClicked() {
    }

    @Test
    fun onCurrencySelected() {
    }

    @Test
    fun onSettingsButtonClicked() {
    }

    @Test
    fun onKeyClicked() {
    }
}