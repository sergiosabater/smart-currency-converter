package com.sergiosabater.smartcurrencyconverter.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sergiosabater.smartcurrencyconverter.viewmodel.SettingsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel, navController: NavController) {
    val userPreferences by settingsViewModel.userPreferencesFlow.collectAsState()
    val soundEnabled = userPreferences.soundEnabled

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Navegar atrás")
                    }
                    Text(
                        text = "Ajustes",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
                SettingsContent(soundEnabled, settingsViewModel, LocalContext.current)
            }
        }
    )
}

@Composable
fun SettingsContent(soundEnabled: Boolean, settingsViewModel: SettingsViewModel, context: Context) {
    val previousSoundEnabled = remember { mutableStateOf(soundEnabled) }

    LaunchedEffect(soundEnabled) {
        if (previousSoundEnabled.value != soundEnabled) {
            val message = if (soundEnabled) "Sonido activado" else "Sonido desactivado"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            previousSoundEnabled.value = soundEnabled
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        SettingOption(
            title = "Sonido",
            description = "Activa o desactiva el sonido del teclado",
            checked = soundEnabled,
            onCheckedChange = {
                settingsViewModel.updateSoundEnabled(it)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun SettingOption(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(0.8f)
                .padding(end = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(description, color = Color.Gray)
        }

        Box(
            modifier = Modifier.weight(0.2f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
