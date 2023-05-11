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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sergiosabater.smartcurrencyconverter.model.Currency
import com.sergiosabater.smartcurrencyconverter.util.parser.parseCurrencies


class CurrencySelector {

    private val dropdownTextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )

    @Composable
    fun CustomCurrencySelector(currencies: List<Currency>, onCurrencySelected: (List<Currency>, String) -> Unit) {

        val (isExpanded1, onExpandedChange1) = remember { mutableStateOf(false) }
        val (selectedCurrency1, onSelectedCurrencyChange1) = remember { mutableStateOf(currencies[0].currencyName) }

        val (isExpanded2, onExpandedChange2) = remember { mutableStateOf(false) }
        val (selectedCurrency2, onSelectedCurrencyChange2) = remember { mutableStateOf(currencies[0].currencyName) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomExposedDropdown(
                currencies = currencies,
                isExpanded = isExpanded1,
                onExpandedChange = onExpandedChange1,
                selectedCurrency = selectedCurrency1,
                onSelectedCurrencyChange = { selectedCurrency ->
                    onSelectedCurrencyChange1(selectedCurrency)
                    onCurrencySelected(currencies, selectedCurrency)
                },
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
                currencies = currencies,
                isExpanded = isExpanded2,
                onExpandedChange = onExpandedChange2,
                selectedCurrency = selectedCurrency2,
                onSelectedCurrencyChange = onSelectedCurrencyChange2,
                modifier = Modifier.weight(1f)
            )
        }
    }

    @Composable
    private fun CustomExposedDropdown(
        currencies: List<Currency>,
        isExpanded: Boolean,
        onExpandedChange: (Boolean) -> Unit,
        selectedCurrency: String,
        onSelectedCurrencyChange: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Box(modifier.wrapContentHeight()) {
            Box(
                modifier = modifier.clickable(onClick = { onExpandedChange(true) })
            ) {
                Text(
                    text = selectedCurrency,
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
                currencies.forEach { currency ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectedCurrencyChange(currency.currencyName)
                                onExpandedChange(false)
                            }
                    ) {
                        Text(
                            text = currency.currencyName,
                            style = dropdownTextStyle,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}