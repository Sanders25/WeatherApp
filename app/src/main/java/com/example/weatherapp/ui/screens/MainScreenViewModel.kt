package com.example.weatherapp.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.service.WeatherRepository
import com.example.weatherapp.data.service.dto.location.LocationResponse
import com.example.weatherapp.data.service.dto.weather.WeatherResponse
import com.example.weatherapp.ui.theme.WeatherAppStyles
import com.example.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val repository: WeatherRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    private val _tempDisplayState = MutableStateFlow(TempDisplayState.Feeling)
    val tempDisplayState: StateFlow<TempDisplayState> = _tempDisplayState

    val weather: Flow<WeatherResponse?> = repository.currentLocationWeather(appContext)
    val location: Flow<LocationResponse?> = repository.currentLocation(appContext)


    init {

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