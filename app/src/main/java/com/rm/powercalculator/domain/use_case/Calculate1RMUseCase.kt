package com.rm.powercalculator.domain.use_case

import com.rm.powercalculator.domain.model.FormulaType

class Calculate1RMUseCase {
    operator fun invoke(weight: String, reps: String, formulaType: FormulaType): Result<Double> {
        val weightDouble = weight.toDoubleOrNull()
        val repsInt = reps.toIntOrNull()

        if (weightDouble == null || repsInt == null || repsInt == 0) {
            return Result.failure(IllegalArgumentException("Invalid weight or reps. Please ensure they are valid numbers and reps are not zero."))
        }

        val oneRepMax = when (formulaType) {
            FormulaType.BRZYCKI -> weightDouble / (1.0278 - 0.0278 * repsInt)
            FormulaType.EPLEY -> weightDouble * (1 + repsInt / 30.0)
        }
        
        return Result.success(oneRepMax)
    }
}
