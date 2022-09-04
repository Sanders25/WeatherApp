package com.example.weatherapp.ui.screens

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.local.WeatherLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val localRepository: WeatherLocalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    private val _tempDisplayState = MutableStateFlow(TempDisplayState.Feeling)
    val tempDisplayState: StateFlow<TempDisplayState> = _tempDisplayState

    fun onEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.SwipeRight -> {

            }
            UiEvent.ChangeTempDisplay -> {
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