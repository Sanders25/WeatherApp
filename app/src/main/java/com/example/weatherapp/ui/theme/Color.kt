package com.example.weatherapp.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient

val ClearPalette = WeatherAppColors(
    primaryBackground = Color(0xFFFFFFFF),
    primaryText = Color(0xFF3D454C),
    secondaryBackground = Color(0xFFF3F4F5),
    secondaryText = Color(0xCC7A8A99),
    tintColor = Color(0xFF1463FF),
    controlColor = Color(0xFF7A8A99),
    errorColor = Color(0xFFFF3377),
)

val CloudyPalette = WeatherAppColors(
    primaryBackground = Color(0xFF23282D),
    primaryText = Color(0xFFF2F4F5),
    secondaryBackground = Color(0xFF191E23),
    secondaryText = Color(0xCC7A8A99),
    tintColor = Color(0xFF4590B0),
    controlColor = Color(0xFF7A8A99),
    errorColor = Color(0xFFFF6699)
)

val RainyPalette = WeatherAppColors(
    primaryBackground = Color(0xFF23282D),
    primaryText = Color(0xFFF2F4F5),
    secondaryBackground = Color(0xFF191E23),
    secondaryText = Color(0xCC7A8A99),
    tintColor = Color(0xFF14278E),
    controlColor = Color(0xFF7A8A99),
    errorColor = Color(0xFFFF6699)
)

object Gradients {
    val clearGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1463FF),
            Color(0xFF176BFF),
            Color(0xFF2E9BFF)
        )
    )

    val cloudyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4590B0),
            Color(0xFF2C5C88)
        )
    )

    val rainyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF14278E),
            Color(0x8814278E)
        )
    )
}