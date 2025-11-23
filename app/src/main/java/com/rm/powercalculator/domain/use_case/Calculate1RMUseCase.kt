package com.rm.powercalculator.domain.use_case

class Calculate1RMUseCase {
    operator fun invoke(weight: String, reps: String): Result<Double> {
        val weightDouble = weight.toDoubleOrNull()
        val repsInt = reps.toIntOrNull()

        if (weightDouble == null || repsInt == null || repsInt == 0) {
            return Result.failure(IllegalArgumentException("Invalid weight or reps. Please ensure they are valid numbers and reps are not zero."))
        }

        // Brzycki formula
        val oneRepMax = weightDouble / (1.0278 - 0.0278 * repsInt)
        return Result.success(oneRepMax)
    }
}
