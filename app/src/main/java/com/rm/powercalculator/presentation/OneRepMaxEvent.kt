package com.rm.powercalculator.presentation

sealed class OneRepMaxEvent {
    data class OnWeightChange(val weight: String) : OneRepMaxEvent()
    data class OnRepsChange(val reps: String) : OneRepMaxEvent()
    object OnCalculateClick : OneRepMaxEvent()
}
