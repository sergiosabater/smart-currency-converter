package com.sergiosabater.smartcurrencyconverter.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class CurrencyDropdown {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable

    fun CustomDropdown() {
        val countries = listOf("Estados Unidos", "Canadá", "México", "España", "Francia")

        var isExpanded by remember {
            mutableStateOf(false)
        }

        var selectedCountry by remember {
            mutableStateOf(countries[0])
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it })
            {
                TextField(
                    value = selectedCountry,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .padding(vertical = 0.dp) // Elimina el espacio vertical alrededor del TextField
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    countries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(text = country) },
                            onClick = {
                                selectedCountry = country
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
