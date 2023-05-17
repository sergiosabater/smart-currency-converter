package com.sergiosabater.smartcurrencyconverter.domain.usecase.common

import androidx.navigation.NavController

class NavigateToSettingsUseCase(private val navController: NavController) {
    fun execute() {
        navController.navigate("settings")
    }
}