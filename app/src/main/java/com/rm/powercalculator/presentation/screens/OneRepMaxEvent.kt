package com.rm.powercalculator.presentation.screens

import com.rm.powercalculator.domain.model.Calculation

sealed class OneRepMaxEvent {
    data class OnExerciseNameChange(val name: String) : OneRepMaxEvent()
    data class OnWeightChange(val weight: String) : OneRepMaxEvent()
    data class OnRepsChange(val reps: String) : OneRepMaxEvent()
    object OnCalculateClick : OneRepMaxEvent()
    data class DeleteHistory(val calculation: Calculation) : OneRepMaxEvent()
}
