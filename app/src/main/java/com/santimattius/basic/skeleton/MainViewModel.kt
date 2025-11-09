package com.santimattius.basic.skeleton

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

data class MainUiState(
    val isLoading: Boolean = false,
    val message: String = "",
)

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state.onStart {
        sayHello()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState()
    )

    fun sayHello() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val message = if (Random.nextBoolean()) "Hello, Android!" else "Goodbye, Android!"
            _state.update { it.copy(isLoading = false, message = message) }
        }
    }
}