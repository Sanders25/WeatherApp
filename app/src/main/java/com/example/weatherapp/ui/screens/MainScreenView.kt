@file:OptIn(ExperimentalMaterialApi::class)

package com.example.weatherapp.ui.screens

import android.graphics.drawable.Icon
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import com.example.weatherapp.ui.theme.WeatherAppCorners
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.theme.WeatherAppTypography
import com.example.weatherapp.ui.theme.gradients
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun TodayScreen(viewModel: MainScreenViewModel = hiltViewModel()) {

    when (viewModel.uiState.collectAsState().value) {
        is MainScreenViewModel.UiState.Idle -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp, start = 20.dp, end = 20.dp)
            ) {
                LocationHeader(Modifier.padding(bottom = 20.dp))
                WeatherCard(viewModel.tempDisplayState.collectAsState().value, viewModel::onEvent)
                ForecastCard()
            }
        }
        else -> {}
    }
}

@Composable
fun LocationHeader(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_location_on_24),
            contentDescription = "Current location"
        )
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "St. Petersburg",
                style = WeatherAppTheme.typography.header,
                modifier = Modifier.align(Alignment.TopStart)
            )
            Text(
                text = "Avtovo distr.",
                style = WeatherAppTheme.typography.caption,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}

@Composable
fun WeatherCard(
    tempDisplayState: TempDisplayState,
    onEvent: (UiEvent) -> Unit,
    modifier: Modifier = Modifier,
    //content: @Composable () -> Unit
) {

//    var currentTempDisplayState by remember { mutableStateOf(tempDisplayState) }


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
                .background(gradients.middayGradient)
                .padding(vertical = 20.dp)
        ) {
            Text(
                "Saturday, 23 Oct",
                style = WeatherAppTheme.typography.main,
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Crossfade(
                targetState = tempDisplayState
            ) { state ->
                when (state) {
                    TempDisplayState.Actual -> {
                        Text(
                            text = "Actual",
                            style = WeatherAppTheme.typography.main,
                            fontSize = 14.sp,
                        )
                    }
                    TempDisplayState.Feeling -> {
                        Text(
                            text = "Feels like",
                            style = WeatherAppTheme.typography.main,
                            fontSize = 14.sp,
                        )
                    }
                }
            }

            val swipeableSpace = 90.dp
            val swipeableState = rememberSwipeableState(tempDisplayState)
            val sizePx = with(LocalDensity.current) { swipeableSpace.toPx() }
            val anchors = mapOf(0f to TempDisplayState.Feeling, sizePx to TempDisplayState.Actual)
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
                    .offset(-swipeableSpace + 50.dp, 0.dp)
            ) {
                Row(Modifier.offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }) {
                    Text(
                        text = "22 C°",
                        style = WeatherAppTheme.typography.accent,
                        color = Color.White,
                        modifier = Modifier
                            .graphicsLayer {
                                alpha = 0f + swipeableState.offset.value / 100
                            }
                    )
                    Text(
                        text = "33 C°",
                        style = WeatherAppTheme.typography.accent,
                        color = Color.White,
                        modifier = Modifier
                            .graphicsLayer {
                                alpha = 1f - swipeableState.offset.value / 100
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
            Icon(
                painter = painterResource(id = R.drawable.ic_rainy),
                contentDescription = "Rainy",
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = "Rainy",
                style = WeatherAppTheme.typography.main,
                fontSize = 40.sp
            )
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
            modifier = Modifier.background(gradients.middayGradient)
        ) {
            Text(text = hour)
            Icon(painter = icon, contentDescription = "", modifier = Modifier.size(20.dp))
            Text(text = "Now")
        }
    }
}