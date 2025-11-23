package com.rm.powercalculator.presentation.screens

import com.rm.powercalculator.domain.model.Calculation
import com.rm.powercalculator.domain.model.FormulaType
import com.rm.powercalculator.domain.model.PercentageBasedCalculation

data class OneRepMaxScreenState(
    val exerciseName: String = "",
    val weightInput: String = "",
    val repsInput: String = "",
    val oneRepMax: Double? = null,
    val history: List<Calculation> = emptyList(),
    val percentages: List<PercentageBasedCalculation> = emptyList(),
    val formulaType: FormulaType = FormulaType.BRZYCKI
)
