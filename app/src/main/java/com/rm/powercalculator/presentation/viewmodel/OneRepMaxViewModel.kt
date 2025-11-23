package com.rm.powercalculator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.powercalculator.domain.model.Calculation
import com.rm.powercalculator.domain.use_case.Calculate1RMUseCase
import com.rm.powercalculator.domain.use_case.DeleteHistoryUseCase
import com.rm.powercalculator.domain.use_case.GetHistoryUseCase
import com.rm.powercalculator.domain.use_case.SaveHistoryUseCase
import com.rm.powercalculator.presentation.screens.OneRepMaxEvent
import com.rm.powercalculator.presentation.screens.OneRepMaxScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OneRepMaxViewModel @Inject constructor(
    private val calculate1RMUseCase: Calculate1RMUseCase,
    private val saveHistoryUseCase: SaveHistoryUseCase,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OneRepMaxScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        getHistoryUseCase().onEach { history ->
            _uiState.update { it.copy(history = history) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: OneRepMaxEvent) {
        when (event) {
            is OneRepMaxEvent.OnExerciseNameChange -> {
                _uiState.update { it.copy(exerciseName = event.name) }
            }
            is OneRepMaxEvent.OnWeightChange -> {
                _uiState.update { it.copy(weightInput = event.weight) }
            }
            is OneRepMaxEvent.OnRepsChange -> {
                _uiState.update { it.copy(repsInput = event.reps) }
            }
            OneRepMaxEvent.OnCalculateClick -> {
                calculateOneRepMax()
            }
            is OneRepMaxEvent.DeleteHistory -> {
                viewModelScope.launch {
                    deleteHistoryUseCase(event.calculation)
                }
            }
        }
    }

    private fun calculateOneRepMax() {
        val exerciseName = _uiState.value.exerciseName
        val weight = _uiState.value.weightInput
        val reps = _uiState.value.repsInput
        val result = calculate1RMUseCase(weight, reps)

        result.onSuccess { oneRepMax ->
            _uiState.update { it.copy(oneRepMax = oneRepMax) }
            viewModelScope.launch {
                saveHistoryUseCase(
                    Calculation(
                        exerciseName = exerciseName,
                        weight = weight.toDouble(),
                        reps = reps.toInt(),
                        oneRepMax = oneRepMax,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }.onFailure {
            // Handle error - maybe show a snackbar
        }
    }
}
