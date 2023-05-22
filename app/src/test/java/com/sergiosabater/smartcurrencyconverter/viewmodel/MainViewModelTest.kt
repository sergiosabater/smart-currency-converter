package com.sergiosabater.smartcurrencyconverter.viewmodel

import android.app.Application
import android.content.res.Resources
import app.cash.turbine.test
import com.sergiosabater.smartcurrencyconverter.data.network.ApiResult
import com.sergiosabater.smartcurrencyconverter.domain.model.CurrencyResult
import com.sergiosabater.smartcurrencyconverter.domain.usecase.common.NavigateToSettingsUseCase
import com.sergiosabater.smartcurrencyconverter.repository.CurrencyRepository
import com.sergiosabater.smartcurrencyconverter.viewmodel.TestHelpers.jsonString
import com.sergiosabater.smartcurrencyconverter.viewmodel.TestHelpers.response
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    // Dependencias mockeadas
    private lateinit var application: Application
    private lateinit var mainViewModel: MainViewModel

    private val mockCurrencyRepository = mockk<CurrencyRepository>()
    private val mockNavigateToSettingsUseCase = mockk<NavigateToSettingsUseCase>()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Mockea el contexto de la aplicación
        application = mockk<Application>(relaxed = true) {
            every { applicationContext } returns this
        }

        // Mockea el objeto Resources y el InputStream
        val resources = mockk<Resources>()
        val inputStream =
            ByteArrayInputStream(jsonString.toByteArray(Charset.defaultCharset()))

        // Configura el objeto Application mockeado para que devuelva el objeto Resources mockeado
        every { application.resources } returns resources

        // Configura el objeto Resources mockeado para que devuelva el InputStream cuando se llame a openRawResource()
        every { resources.openRawResource(any()) } returns inputStream

        // Crea la instancia del ViewModel con las dependencias mockeadas
        mainViewModel =
            MainViewModel(application, mockCurrencyRepository, mockNavigateToSettingsUseCase)
    }

    @After
    fun tearDown() {
        //Limpiamos Dispatchers
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun loadCurrencies() {
        // Configurar las respuestas de las dependencias mockeadas
        coEvery { mockCurrencyRepository.getCurrencyRates() } returns ApiResult.Success(response)

        // Creamos el resultado esperado
        val expectedResult = CurrencyResult.Success(TestHelpers.loadCurrenciesFromApi(response))

        runTest {
            // Llamar al método a testear
            mainViewModel.loadCurrencies()

        }

        // Los resultados obtenidos
        val result = mainViewModel.currencies.value

        // Aquí verificamos que 'result' contiene los datos esperados.
        assertEquals(expectedResult, result)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `test onClearButtonClicked`() = coroutineTestRule.runTest {

        mainViewModel.onNumericButtonClicked("8")

        // When
        mainViewModel.onClearButtonClicked()

        // Then
        mainViewModel.displayText.test {
            assertTrue(awaitItem() == "0")
            cancelAndIgnoreRemainingEvents()
        }
    }

    /*@Test
    fun testOnClearButtonClicked() {

        // Establecer un valor inicial para _displayText
        mainViewModel._displayText.value = "1234"

        mainViewModel.onClearButtonClicked()

        // Assert que _displayText es igual a "0" después de llamar a onClearButtonClicked
        assertEquals("0", mainViewModel._displayText.value)
    }
    */
}