package com.example.weatherapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

data class WeatherAppColors(
    val primaryText: Color,
    val primaryBackground: Color,
    val secondaryText: Color,
    val secondaryBackground: Color,
    val tintColor: Color,
    val controlColor: Color,
    val errorColor: Color
)

data class WeatherAppTypography(
    val header: TextStyle,
    val body: TextStyle,
    val caption: TextStyle,
    val main:TextStyle,
    val accent: TextStyle
)

data class WeatherAppShapes(
    val padding: Dp,
    val cornersStyle: Shape
)

object WeatherAppTheme {
    val colors: WeatherAppColors
        @Composable
        get() = LocalWeatherAppColors.current

    val typography: WeatherAppTypography
        @Composable
        get() = LocalWeatherAppTypography.current

    val shapes: WeatherAppShapes
        @Composable
        get() = LocalWeatherAppShapes.current
}

enum class WeatherAppStyles {
    Clear, Cloudy, Rainy
}

enum class WeatherAppSizes {
    Small, Medium, Big
}

enum class WeatherAppCorners {
    Flat, Rounded
}

val LocalWeatherAppColors = staticCompositionLocalOf<WeatherAppColors> {
    error("No colors provided")
}

val LocalWeatherAppTypography = staticCompositionLocalOf<WeatherAppTypography> {
    error("No font provided")
}

val LocalWeatherAppShapes = staticCompositionLocalOf<WeatherAppShapes> {
    error("No shapes provided")
}