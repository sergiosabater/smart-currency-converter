package com.sergiosabater.smartcurrencyconverter.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sergiosabater.smartcurrencyconverter.R

@Composable
fun ErrorScreen(
    title: String = "Error de conexión",
    subtitle: String = "Debe tener acceso a Internet"
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.error_image),
                contentDescription = "Error Image",
                modifier = Modifier.size(100.dp) // Ajusta el tamaño según lo necesario
            )
            Spacer(modifier = Modifier.height(20.dp)) // Proporciona un espacio entre la imagen y el texto
            Text(
                text = title,
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp)) // Proporciona un espacio entre el título y el subtítulo
            Text(
                text = subtitle,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 18.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewErrorScreen() {
    ErrorScreen()
}
