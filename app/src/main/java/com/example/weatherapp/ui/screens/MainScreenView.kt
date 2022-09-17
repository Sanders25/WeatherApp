@file:OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)

package com.example.weatherapp.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.service.dto.location.LocationResponse
import com.example.weatherapp.data.service.dto.weather.WeatherResponse
import com.example.weatherapp.ui.theme.Gradients
import com.example.weatherapp.ui.theme.WeatherAppStyles
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import kotlin.math.roundToInt

@Composable
fun TodayScreen(viewModel: MainScreenViewModel = hiltViewModel()) {

    val weather by viewModel.weather.collectAsState(initial = null)
    val location by viewModel.location.collectAsState(initial = null)

    when (viewModel.uiState.collectAsState().value) {
        is MainScreenViewModel.UiState.Idle -> {
            WeatherAppTheme(style = weather?.style() ?: WeatherAppStyles.Clear) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 80.dp, start = 20.dp, end = 20.dp)
                ) {
                    LocationHeader(
                        location,
                        weather?.name,
                        Modifier.padding(bottom = 20.dp)
                    )
                    WeatherCard(
                        weather,
                        viewModel.tempDisplayState.collectAsState().value,
                        viewModel::onEvent,
                    )
                    ForecastCard()
                }
            }
        }
        else -> {}
    }
}

@Composable
fun LocationHeader(
    location: LocationResponse?,
    subLocation: String?,
    modifier: Modifier = Modifier
) {
    val subLocationName = subLocation ?: "..."
    val localLocationName = location?.first()?.localNames?.ru ?: "..."

    Row(
        verticalAlignment = CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_location_on_24),
            contentDescription = "Current location"
        )
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = localLocationName,
                style = WeatherAppTheme.typography.header,
                modifier = Modifier.align(Alignment.TopStart)
            )

            Text(
                text = subLocationName,
                style = WeatherAppTheme.typography.caption,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}

@Composable
fun WeatherCard(
    weather: WeatherResponse?,
    tempDisplayState: TempDisplayState,
    onEvent: (UiEvent) -> Unit,
    modifier: Modifier = Modifier,
    //content: @Composable () -> Unit
) {
    val date: String

    with(LocalDate.now()) {
        date = "${this.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}, " +
                "${this.dayOfMonth} " +
                this.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    var actualTemp = "--"
    var fellingTemp = "--"
    var weatherName = "--"

    weather?.let {
        actualTemp = it.main.temp.roundToInt().toString()
        fellingTemp = it.main.temp.roundToInt().toString()
        weatherName = it.weather.first().description
    }

    // region Animation variables
    val waveWidth = 800
    val originalY = 120f
    val path = Path()

    val deltaXAnim = rememberInfiniteTransition()
    val dx by deltaXAnim.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val dx2 by deltaXAnim.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(1500)
        )
    )
    val deltaYAnim = rememberInfiniteTransition()
    val waveHeight by deltaYAnim.animateFloat(
        initialValue = 80f,//50
        targetValue = 140f,//140
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val waveHeight2 by deltaYAnim.animateFloat(
        initialValue = 100f,//100
        targetValue = 145f,//150
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // endregion

    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(weather?.background() ?: Gradients.clearGradient)
                .padding(top = 20.dp)

        ) {
            Text(
                text = date,
                style = WeatherAppTheme.typography.main,
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 40.dp)
            )
            Crossfade(
                targetState = tempDisplayState
            ) { state ->
                when (state) {
                    TempDisplayState.Actual -> {
                        Text(
                            text = "Реальная",
                            style = WeatherAppTheme.typography.main,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    TempDisplayState.Feeling -> {
                        Text(
                            text = "Ощущается как",
                            style = WeatherAppTheme.typography.main,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()

                        )
                    }
                }
            }

            val swipeableSpace = 90.dp
            val swipeableState = rememberSwipeableState(tempDisplayState)
            val sizePx = with(LocalDensity.current) { swipeableSpace.toPx() }
            val anchors =
                mapOf(0f to TempDisplayState.Feeling, -sizePx to TempDisplayState.Actual)
            //val scope = rememberCoroutineScope()
            if (swipeableState.isAnimationRunning) {
                DisposableEffect(Unit) {
                    onDispose {
                        if (swipeableState.progress.fraction > 0f)
                            onEvent(UiEvent.ChangeTempDisplay)
/*                        when (swipeableState.currentValue) {
                            TempDisplayState.Actual -> {
                                onEvent(UiEvent.ChangeTempDisplay)
                            }
                            TempDisplayState.Feeling -> {
                                onEvent(UiEvent.ChangeTempDisplay)
                            }
                            else -> {
                                return@onDispose
                            }
                        }*/
/*                        scope.launch {
                            // in your real app if you don't have to display offset,
                            // snap without animation
                            // swipeableState.snapTo(SwipeDirection.Initial)
                            swipeableState.animateTo(tempDisplayState)
                        }*/
                    }
                }
            }
            Box(
                Modifier
                    //.width(swipeableSpace)
                    .swipeable(
                        state = swipeableState,
                        anchors = anchors,
                        thresholds = { _, _ -> FractionalThreshold(0.3f) },
                        orientation = Orientation.Horizontal
                    )
                    .offset(-swipeableSpace + 144.dp, 0.dp)
            ) {
                Row(Modifier.offset {
                    IntOffset(
                        swipeableState.offset.value.roundToInt(),
                        0
                    )
                }) {
                    Text(
                        text = "$fellingTemp C°",
                        style = WeatherAppTheme.typography.accent,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .graphicsLayer {
                                alpha = 1f + swipeableState.offset.value / 100
                            }
                    )
                    Text(
                        text = "$actualTemp C°",
                        style = WeatherAppTheme.typography.accent,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier
                            .graphicsLayer {
                                alpha = 0f - swipeableState.offset.value / 100
                            }
                    )
                }
            }
            Row(
                Modifier.padding(bottom = 15.dp, top = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Crossfade(targetState = tempDisplayState) { state ->
                    when (state) {
                        TempDisplayState.Actual -> {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_outline_lens_24),
                                contentDescription = "Actual",
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                        TempDisplayState.Feeling -> {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_lens_24),
                                contentDescription = "Feels like",
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }
                Crossfade(targetState = tempDisplayState) { state ->
                    when (state) {
                        TempDisplayState.Actual -> {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_lens_24),
                                contentDescription = "Feels like",
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                        TempDisplayState.Feeling -> {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_outline_lens_24),
                                contentDescription = "Actual",
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }
/*                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_lens_24),
                    contentDescription = "Feels like",
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_lens_24),
                    contentDescription = "Actual",
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )*/
            }
            if (weather != null) {
                Icon(
                    painter = painterResource(id = weather.icon()),
                    contentDescription = "Rainy",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }
            Text(
                text = weatherName,
                style = WeatherAppTheme.typography.main,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Box() {
                Card(backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .drawBehind {
//region Waves
                            drawPath(
                                path = path,
                                color = Color.White.copy(alpha = 1f)
                            )
                            path.reset()
                            val halfWaveWidth = waveWidth / 2
                            path.moveTo(-waveWidth + (waveWidth * dx2), originalY.dp.toPx())

                            for (i in -waveWidth..(size.width.toInt() + waveWidth) step waveWidth) {
                                path.relativeQuadraticBezierTo(
                                    halfWaveWidth.toFloat() / 2,
                                    -waveHeight2,
                                    halfWaveWidth.toFloat(),
                                    0f
                                )
                                path.relativeQuadraticBezierTo(
                                    halfWaveWidth.toFloat() / 2,
                                    waveHeight2,
                                    halfWaveWidth.toFloat(),
                                    0f
                                )
                            }

                            path.lineTo(size.width, size.height)
                            path.lineTo(0f, size.height)
                            path.close()

                            drawPath(path = path, color = Color.White.copy(alpha = 0.3f))

                            path.reset()
                            path.moveTo(-waveWidth + (waveWidth * dx), originalY.dp.toPx())

                            for (i in -waveWidth..(size.width.toInt() + waveWidth) step waveWidth) {
                                path.relativeQuadraticBezierTo(
                                    halfWaveWidth.toFloat() / 2,
                                    -waveHeight,
                                    halfWaveWidth.toFloat(),
                                    0f
                                )
                                path.relativeQuadraticBezierTo(
                                    halfWaveWidth.toFloat() / 2,
                                    waveHeight,
                                    halfWaveWidth.toFloat(),
                                    0f
                                )
                            }

                            path.lineTo(size.width, size.height)
                            path.lineTo(0f, size.height)
                            path.close()
                        }
                    // endregion
                ) { Unit }

/*                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = 2.dp,
                    backgroundColor = Color.White.copy(alpha = 1f),
                    modifier = Modifier.height(120.dp).fillMaxWidth().align(Alignment.BottomCenter).padding(14.dp)
                ) {

                }*/
            }
        }
    }
}

@Preview
@Composable
fun ForecastCard(
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column {
            Row {
                Text(text = "Today")
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Next 7 days",
                    color = Color.White.copy(alpha = 0.7f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_keyboard_arrow_right_24),
                    contentDescription = "Forecast",
                    tint = Color.White.copy(alpha = 0.7f)
                )
            }
            Row(Modifier.fillMaxWidth()) {
                ForecastPill(
                    hour = "18:00",
                    icon = painterResource(id = R.drawable.ic_rainy),
                    temp = "Now"
                )
                ForecastPill(
                    hour = "19:00",
                    icon = painterResource(id = R.drawable.ic_rainy),
                    temp = "24°"
                )
            }
        }
    }
}

@Composable
fun ForecastPill(
    hour: String,
    icon: Painter,
    temp: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = WeatherAppTheme.shapes.cornersStyle,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(WeatherAppTheme.colors.tintColor)
        ) {
            Text(text = hour)
            Icon(painter = icon, contentDescription = "", modifier = Modifier.size(20.dp))
            Text(text = "Now")
        }
    }
}

private fun WeatherResponse.icon(): Int {
    val condition = weather.first().main

    return when {
        condition.contains("cloud", ignoreCase = true) -> R.drawable.ic_cloudy
        condition.contains("rain", ignoreCase = true) -> R.drawable.ic_rainy
        else -> R.drawable.ic_clear
    }
}

private fun WeatherResponse.style(): WeatherAppStyles {
    val condition = weather.first().main
    return when {
        condition.contains("cloud", ignoreCase = true) -> WeatherAppStyles.Cloudy
        condition.contains("rain", ignoreCase = true) -> WeatherAppStyles.Rainy
        else -> WeatherAppStyles.Clear
    }
}

private fun WeatherResponse.background(): Brush {
    val condition = weather.first().main
    return when {
        condition.contains("cloud", ignoreCase = true) -> Gradients.cloudyGradient
        condition.contains("rain", ignoreCase = true) -> Gradients.rainyGradient
        else -> Gradients.clearGradient
    }
}