package com.sergiosabater.smartcurrencyconverter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Display {

    @Composable
    fun CustomDisplay(displayText: String) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp)
        ) {
            Text(
                text = displayText,
                fontWeight = FontWeight.Light,
                fontSize = 60.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}