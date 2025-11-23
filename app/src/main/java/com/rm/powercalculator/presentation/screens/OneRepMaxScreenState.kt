package com.rm.powercalculator.presentation.screens

import com.rm.powercalculator.domain.model.Calculation

data class OneRepMaxScreenState(
    val exerciseName: String = "",
    val weightInput: String = "",
    val repsInput: String = "",
    val oneRepMax: Double? = null,
    val history: List<Calculation> = emptyList()
)
