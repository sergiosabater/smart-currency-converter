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
import com.sergiosabater.smartcurrencyconverter.domain.model.Currency


class CurrencySelector {

    private val dropdownTextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )

    @Composable
    fun CustomCurrencySelector(
        currencies: List<Currency>,
        onCurrencySelected: (Currency, Currency) -> Unit,
        defaultCurrency1: Currency,
        defaultCurrency2: Currency
    ) {
        val (selectedCurrency1, setSelectedCurrency1) = remember { mutableStateOf(defaultCurrency1) }
        val (selectedCurrency2, setSelectedCurrency2) = remember { mutableStateOf(defaultCurrency2) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomExposedDropdown(
                currencies = currencies,
                selectedCurrency = selectedCurrency1,
                onSelectedCurrencyChange = { selectedCurrency ->
                    setSelectedCurrency1(selectedCurrency)
                    onCurrencySelected(selectedCurrency, selectedCurrency2)
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
                selectedCurrency = selectedCurrency2,
                onSelectedCurrencyChange = { selectedCurrency ->
                    setSelectedCurrency2(selectedCurrency)
                    onCurrencySelected(selectedCurrency1, selectedCurrency)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }

    @Composable
    private fun CustomExposedDropdown(
        currencies: List<Currency>,
        selectedCurrency: Currency,
        onSelectedCurrencyChange: (Currency) -> Unit,
        modifier: Modifier = Modifier
    ) {
        val (isExpanded, setExpanded) = remember { mutableStateOf(false) }

        Box(modifier.wrapContentHeight()) {
            Box(
                modifier = modifier.clickable(onClick = { setExpanded(true) })
            ) {
                Text(
                    text = selectedCurrency.currencyName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(start = 15.dp, top = 15.dp, bottom = 15.dp, end = 15.dp)
                        .align(Alignment.Center)
                )
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { setExpanded(false) }
            ) {
                currencies.forEach { currency ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectedCurrencyChange(currency)
                                setExpanded(false)
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