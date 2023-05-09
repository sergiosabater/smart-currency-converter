package com.sergiosabater.smartcurrencyconverter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class CurrencyDropdown {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CustomDropdown() {
        val countries = listOf("Estados Unidos", "Canadá", "México", "España", "Francia")

        var isExpanded1 by remember { mutableStateOf(false) }
        var isExpanded2 by remember { mutableStateOf(false) }

        var selectedCountry1 by remember { mutableStateOf(countries[0]) }
        var selectedCountry2 by remember { mutableStateOf(countries[0]) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomExposedDropdown(
                countries = countries,
                isExpanded = isExpanded1,
                onExpandedChange = { isExpanded1 = it },
                selectedCountry = selectedCountry1,
                onSelectedCountryChange = { selectedCountry1 = it },
                modifier = Modifier.weight(1f) // Aplica el modificador weight para dividir el ancho disponible
            )

            Spacer(modifier = Modifier.width(16.dp)) // Agrega un espacio entre los dos dropdowns

            CustomExposedDropdown(
                countries = countries,
                isExpanded = isExpanded2,
                onExpandedChange = { isExpanded2 = it },
                selectedCountry = selectedCountry2,
                onSelectedCountryChange = { selectedCountry2 = it },
                modifier = Modifier.weight(1f) // Aplica el modificador weight para dividir el ancho disponible
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CustomExposedDropdown(
        countries: List<String>,
        isExpanded: Boolean,
        onExpandedChange: (Boolean) -> Unit,
        selectedCountry: String,
        onSelectedCountryChange: (String) -> Unit,
        modifier: Modifier = Modifier // Agrega un parámetro de modificador
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = onExpandedChange,
            modifier = modifier // Usa el modificador pasado como parámetro
        ) {
            TextField(
                value = selectedCountry,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(text = country) },
                        onClick = {
                            onSelectedCountryChange(country)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}