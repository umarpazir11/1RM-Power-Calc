package com.rm.powercalculator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.powercalculator.domain.model.Calculation
import com.rm.powercalculator.domain.model.FormulaType
import com.rm.powercalculator.domain.use_case.Calculate1RMUseCase
import com.rm.powercalculator.domain.use_case.CalculatePercentagesUseCase
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
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val calculatePercentagesUseCase: CalculatePercentagesUseCase
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
            is OneRepMaxEvent.OnFormulaChange -> {
                _uiState.update { it.copy(formulaType = event.formulaType) }
            }
        }
    }

    private fun calculateOneRepMax() {
        val state = _uiState.value
        val result = calculate1RMUseCase(state.weightInput, state.repsInput, state.formulaType)

        result.onSuccess { oneRepMax ->
            val percentages = calculatePercentagesUseCase(oneRepMax)
            _uiState.update { it.copy(oneRepMax = oneRepMax, percentages = percentages) }
            viewModelScope.launch {
                saveHistoryUseCase(
                    Calculation(
                        exerciseName = state.exerciseName,
                        weight = state.weightInput.toDouble(),
                        reps = state.repsInput.toInt(),
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
