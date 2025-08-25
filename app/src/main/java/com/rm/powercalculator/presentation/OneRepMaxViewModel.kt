package com.rm.powercalculator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rm.powercalculator.data.HistoryRecordEntity
import com.rm.powercalculator.domain.AddHistoryRecordUseCase
import com.rm.powercalculator.domain.CalculateOneRepMaxUseCase
import com.rm.powercalculator.domain.DeleteHistoryRecordUseCase
import com.rm.powercalculator.domain.GetHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class OneRepMaxViewModel(
    private val calculateOneRepMaxUseCase: CalculateOneRepMaxUseCase,
    private val addHistoryRecordUseCase: AddHistoryRecordUseCase,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val deleteHistoryRecordUseCase: DeleteHistoryRecordUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(OneRepMaxState())
    val state: StateFlow<OneRepMaxState> = _state.asStateFlow()
    
    private val _history = MutableStateFlow<List<HistoryRecordEntity>>(emptyList())
    val history: StateFlow<List<HistoryRecordEntity>> = _history.asStateFlow()
    
    private val _showUndoSnackbar = MutableStateFlow<HistoryRecordEntity?>(null)
    val showUndoSnackbar: StateFlow<HistoryRecordEntity?> = _showUndoSnackbar.asStateFlow()
    
    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()
    
    init {
        loadHistory()
    }
    
    private fun loadHistory() {
        viewModelScope.launch {
            getHistoryUseCase().collect { historyRecords ->
                _history.value = historyRecords
            }
        }
    }
    
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
                
                if (estimatedMax > 0.0) {
                    _state.value = _state.value.copy(estimatedMax = estimatedMax)
                    
                    // Check for duplicates before saving
                    val currentHistory = _history.value
                    val isDuplicate = currentHistory.isNotEmpty() && 
                        currentHistory.first().let { lastRecord ->
                            lastRecord.weight == weight && 
                            lastRecord.reps == reps && 
                            lastRecord.oneRepMax == estimatedMax
                        }
                    
                    // Only save if not a duplicate
                    if (!isDuplicate) {
                        viewModelScope.launch {
                            val historyRecord = HistoryRecordEntity(
                                weight = weight,
                                reps = reps,
                                oneRepMax = estimatedMax
                            )
                            addHistoryRecordUseCase(historyRecord)
                        }
                    }
                }
            }
            is OneRepMaxEvent.OnDeleteHistoryRecord -> {
                viewModelScope.launch {
                    deleteHistoryRecordUseCase(event.record)
                    // Emit UI event for snackbar
                    _uiEvent.emit("Calculation deleted")
                    // Show undo snackbar
                    _showUndoSnackbar.value = event.record
                }
            }
            is OneRepMaxEvent.OnUndoDeleteHistoryRecord -> {
                viewModelScope.launch {
                    addHistoryRecordUseCase(event.record)
                    // Hide undo snackbar
                    _showUndoSnackbar.value = null
                }
            }
        }
    }
    
    fun hideUndoSnackbar() {
        _showUndoSnackbar.value = null
    }
}
