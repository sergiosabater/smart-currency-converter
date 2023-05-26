package com.sergiosabater.smartcurrencyconverter.viewmodel

import app.cash.turbine.test
import com.sergiosabater.smartcurrencyconverter.repository.CurrencyRepository
import com.sergiosabater.smartcurrencyconverter.repository.UserPreferencesRepository
import com.sergiosabater.smartcurrencyconverter.util.parser.CurrencyLoader
import com.sergiosabater.smartcurrencyconverter.util.sound.SoundPlayer
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class MainViewModelTest {

    // Crea una regla para las pruebas que cambiará el Dispatchers.Main a un TestDispatcher
    @get:Rule
    var coroutinesTestRule = MainCoroutineRule()

    // Crea los mocks de las dependencias
    private val mockCurrencyRepository = mockk<CurrencyRepository>()
    private val mockUserPreferencesRepository = mockk<UserPreferencesRepository>()
    private val mockSoundPlayer = mockk<SoundPlayer>()
    private val mockCurrencyLoader = mockk<CurrencyLoader>()

    // Crea el ViewModel a probar, con los mocks como dependencias
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        // Antes de cada prueba, reinicia los mocks y crea una nueva instancia del ViewModel
        clearAllMocks()
        viewModel = MainViewModel(
            mockCurrencyRepository,
            mockUserPreferencesRepository,
            mockSoundPlayer,
            mockCurrencyLoader
        )
    }

    // Prueba de un evento simple: hacer clic en el botón de borrado
    @OptIn(ExperimentalTime::class)
    @Test
    fun `test onClearButtonClicked`() = runBlocking {

        // Given
        viewModel.onNumericButtonClicked("8")

        viewModel.uiState.test {
            assertEquals("8", awaitItem().displayText)

            // When
            viewModel.onClearButtonClicked()

            // Then
            assertEquals("0", awaitItem().displayText)
            cancelAndConsumeRemainingEvents()
        }
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `test onNumericButtonClicked to get 1000`() = runBlocking {

        // Given
        viewModel.onNumericButtonClicked("1")

        // When
        viewModel.uiState.test {
            assertEquals("1", awaitItem().displayText)

            viewModel.onNumericButtonClicked("0")
            assertEquals("10", awaitItem().displayText)

            viewModel.onNumericButtonClicked("0")
            assertEquals("100", awaitItem().displayText)

            viewModel.onNumericButtonClicked("0")

            //Then
            assertEquals("1.000", awaitItem().displayText)

            cancelAndConsumeRemainingEvents()
        }

    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `test onBackspaceClicked with existing display value`() = runBlocking {

        // Given a display text with value "123"

        viewModel.onNumericButtonClicked("1")

        viewModel.uiState.test {

            assertEquals("1", awaitItem().displayText)

            viewModel.onNumericButtonClicked("2")
            assertEquals("12", awaitItem().displayText)

            viewModel.onNumericButtonClicked("3")
            assertEquals("123", awaitItem().displayText)

            //When
            viewModel.onBackspaceClicked()

            //Then
            assertEquals("12", awaitItem().displayText)

            cancelAndConsumeRemainingEvents()
        }

    }
}


