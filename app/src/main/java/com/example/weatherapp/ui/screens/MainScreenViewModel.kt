package com.example.weatherapp.ui.screens

import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.local.WeatherLocalRepository
import com.google.android.gms.location.*
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val localRepository: WeatherLocalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    private val _tempDisplayState = MutableStateFlow(TempDisplayState.Feeling)
    val tempDisplayState: StateFlow<TempDisplayState> = _tempDisplayState

    private var lastLocation: Location? = null

    init {
        getLocation(appContext)
    }

    fun onEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.SwipeRight -> {

            }
            is UiEvent.ChangeTempDisplay -> {
                when (_tempDisplayState.value) {
                    TempDisplayState.Actual -> _tempDisplayState.value = TempDisplayState.Feeling
                    TempDisplayState.Feeling -> _tempDisplayState.value = TempDisplayState.Actual
                }
            }
        }
    }

    private fun getLocation(context: Context) {
        val client = LocationServices.getFusedLocationProviderClient(context)
        val request = LocationRequest.create()
            .setInterval(10_000)
            .setFastestInterval(5_000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setSmallestDisplacement(170f)

        client.requestLocationUpdates(request, object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                lastLocation = result.lastLocation
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                println("Location availability: ${availability.isLocationAvailable}")
            }
        },
            Looper.getMainLooper()
        )
    }

    sealed class UiState {
        object Loading : UiState()
        object Idle : UiState()
        object Transitioning : UiState()

        class Error(val message: String) : UiState()
    }
}

sealed class UiEvent {
    object SwipeRight : UiEvent()
    object ChangeTempDisplay : UiEvent()
}

enum class TempDisplayState(val raw: Int) {
    Feeling(0), Actual(1)
}