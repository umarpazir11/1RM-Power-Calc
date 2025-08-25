package com.rm.powercalculator.presentation

import androidx.lifecycle.ViewModel
import com.rm.powercalculator.domain.CalculateOneRepMaxUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OneRepMaxViewModel : ViewModel() {
    
    private val _state = MutableStateFlow(OneRepMaxState())
    val state: StateFlow<OneRepMaxState> = _state.asStateFlow()
    
    private val calculateOneRepMaxUseCase = CalculateOneRepMaxUseCase()
    
    fun onEvent(event: OneRepMaxEvent) {
        when (event) {
            is OneRepMaxEvent.OnWeightChange -> {
                _state.value = _state.value.copy(weightInput = event.weight)
            }
            is OneRepMaxEvent.OnRepsChange -> {
                _state.value = _state.value.copy(repsInput = event.reps)
            }
            is OneRepMaxEvent.OnCalculateClick -> {
                val weight = _state.value.weightInput.toDoubleOrNull() ?: 0.0
                val reps = _state.value.repsInput.toIntOrNull() ?: 0
                val estimatedMax = calculateOneRepMaxUseCase(weight, reps)
                _state.value = _state.value.copy(estimatedMax = estimatedMax)
            }
        }
    }
}
