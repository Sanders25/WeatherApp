@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.weatherapp

import android.Manifest
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.weatherapp.ui.screens.TodayScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(color = WeatherAppTheme.colors.primaryBackground)

                val locationPermissionsState = rememberMultiplePermissionsState(
                    listOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    )
                )

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(
                    key1 = lifecycleOwner,
                    effect = {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_START) {
                                locationPermissionsState.launchMultiplePermissionRequest()
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                )

                if (locationPermissionsState.allPermissionsGranted) {
                    TodayScreen()
                } else {
                    ShowPlaceholder(locationPermissionsState)
                }
            }
        }
    }

    @Composable
    fun ShowPlaceholder(permissionsState: MultiplePermissionsState) {
        // TODO Permissions placeholder
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val allPermissionsRevoked =
                permissionsState.permissions.size ==
                        permissionsState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                "Please allow for location access."
            } else if (permissionsState.shouldShowRationale) {
                "This app requires exact location permission, please restart the app."
            } else {
                "This app requires location permission. Allow location access in your device settings."
            }

            Text(text = textToShow, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 20.dp))
        }
    }
}