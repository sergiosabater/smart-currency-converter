package com.sergiosabater.smartcurrencyconverter.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class CurrencySelector {

    private val dropdownTextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )

    @Composable
    fun CustomCurrencySelector() {
        val countries = remember {
            listOf(
                "Estados Unidos", "Canadá", "México", "España", "Francia",
                "Italia", "Alemania", "Portugal", "Japón", "Australia",
                "Reino Unido", "Irlanda", "Suecia", "Noruega", "Dinamarca",
                "Suiza", "Bélgica", "Países Bajos", "Austria", "Grecia",
                "Finlandia", "Rusia", "China", "India", "Brasil",
                "Argentina", "Colombia", "Chile", "Uruguay", "Perú",
                "Venezuela", "Costa Rica", "Panamá", "Puerto Rico", "República Dominicana",
                "Cuba", "Honduras", "El Salvador", "Nicaragua", "Guatemala"
            )
        }

        /* Variables de par. Usadas para almacenar estados en Compose. Permiten la recomposición
        automática de la UI cuando el estado cambia */
        val (isExpanded1, onExpandedChange1) = remember { mutableStateOf(false) }
        val (selectedCountry1, onSelectedCountryChange1) = remember { mutableStateOf(countries[0]) }

        val (isExpanded2, onExpandedChange2) = remember { mutableStateOf(false) }
        val (selectedCountry2, onSelectedCountryChange2) = remember { mutableStateOf(countries[0]) }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomExposedDropdown(
                countries = countries,
                isExpanded = isExpanded1,
                onExpandedChange = onExpandedChange1,
                selectedCountry = selectedCountry1,
                onSelectedCountryChange = onSelectedCountryChange1,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "➔",
                fontSize = 25.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp)
            )

            CustomExposedDropdown(
                countries = countries,
                isExpanded = isExpanded2,
                onExpandedChange = onExpandedChange2,
                selectedCountry = selectedCountry2,
                onSelectedCountryChange = onSelectedCountryChange2,
                modifier = Modifier.weight(1f)
            )
        }
    }

    @Composable
    private fun CustomExposedDropdown(
        countries: List<String>,
        isExpanded: Boolean,
        onExpandedChange: (Boolean) -> Unit,
        selectedCountry: String,
        onSelectedCountryChange: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Box(modifier.wrapContentHeight()) {
            Box(
                modifier = modifier.clickable(onClick = { onExpandedChange(true) })
            ) {
                Text(
                    text = selectedCountry,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(start = 15.dp, top = 15.dp, bottom = 15.dp, end = 15.dp)
                        .align(Alignment.Center)
                )
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                countries.forEach { country ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectedCountryChange(country)
                                onExpandedChange(false)
                            }
                    ) {
                        Text(
                            text = country,
                            style = dropdownTextStyle,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
