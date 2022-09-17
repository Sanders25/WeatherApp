package com.example.weatherapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R

@Composable
fun WeatherAppTheme(
    style: WeatherAppStyles = WeatherAppStyles.Clear,
    textSize: WeatherAppSizes = WeatherAppSizes.Medium,
    paddingSize: WeatherAppSizes = WeatherAppSizes.Medium,
    corners: WeatherAppCorners = WeatherAppCorners.Rounded,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when (darkTheme) {
        true -> {
            when (style) {
                WeatherAppStyles.Clear -> ClearPalette
                WeatherAppStyles.Cloudy -> CloudyPalette
                WeatherAppStyles.Rainy -> RainyPalette
            }
        }
        false -> {
            when (style) {
                WeatherAppStyles.Clear -> ClearPalette
                WeatherAppStyles.Cloudy -> CloudyPalette
                WeatherAppStyles.Rainy -> RainyPalette
            }
        }
    }

    val Dosis = FontFamily(
        Font(R.font.dosis_extralight, FontWeight.ExtraLight),
        Font(R.font.dosis_light, FontWeight.Light),
        Font(R.font.dosis_regular, FontWeight.Normal),
        Font(R.font.dosis_medium, FontWeight.Medium),
        Font(R.font.dosis_semibold, FontWeight.SemiBold),
        Font(R.font.dosis_bold, FontWeight.Bold),
        Font(R.font.dosis_extrabold, FontWeight.ExtraBold),
    )

    val Comfortaa = FontFamily(
        Font(R.font.comfortaa_light, FontWeight.Light),
        Font(R.font.comfortaa_regular, FontWeight.Normal),
        Font(R.font.comfortaa_medium, FontWeight.Medium),
        Font(R.font.comfortaa_semibold, FontWeight.SemiBold),
        Font(R.font.comfortaa_bold, FontWeight.Bold),
    )

    val typography = WeatherAppTypography(
        header = TextStyle(
            fontSize = when (textSize) {
                WeatherAppSizes.Small -> 24.sp
                WeatherAppSizes.Medium -> 28.sp
                WeatherAppSizes.Big -> 36.sp
            },
            fontFamily = Comfortaa,
            fontWeight = FontWeight.Bold
        ),
        body = TextStyle(
            fontSize = when (textSize) {
                WeatherAppSizes.Small -> 14.sp
                WeatherAppSizes.Medium -> 16.sp
                WeatherAppSizes.Big -> 18.sp
            },
            fontWeight = FontWeight.Normal
        ),
        caption = TextStyle(
            fontSize = when (textSize) {
                WeatherAppSizes.Small -> 10.sp
                WeatherAppSizes.Medium -> 16.sp
                WeatherAppSizes.Big -> 22.sp
            },
            fontFamily = Comfortaa,
            fontWeight = FontWeight.Normal
        ),
        main = TextStyle(
            fontSize = 20.sp,
            fontFamily = Comfortaa,
            fontWeight = FontWeight.Normal,
            color = Color.White
        ),
        accent = TextStyle(
            fontSize = 40.sp,
            fontFamily = Comfortaa,
            fontWeight = FontWeight.Bold
        )

    )

    val shapes = WeatherAppShapes(
        padding = when (paddingSize) {
            WeatherAppSizes.Small -> 12.dp
            WeatherAppSizes.Medium -> 16.dp
            WeatherAppSizes.Big -> 20.dp
        },
        cornersStyle = when (corners) {
            WeatherAppCorners.Flat -> RoundedCornerShape(0.dp)
            WeatherAppCorners.Rounded -> RoundedCornerShape(8.dp)
        }
    )

    CompositionLocalProvider(
        LocalWeatherAppColors provides colors,
        LocalWeatherAppTypography provides typography,
        LocalWeatherAppShapes provides shapes,
        content = content
    )
}