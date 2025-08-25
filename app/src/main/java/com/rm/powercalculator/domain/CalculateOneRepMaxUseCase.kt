package com.rm.powercalculator.domain

class CalculateOneRepMaxUseCase {
    operator fun invoke(weight: Double, reps: Int): Double {
        if (weight <= 0.0 || reps <= 0) return 0.0
        if (reps == 1) return weight
        val denominator = 1.0278 - (0.0278 * reps)
        return if (denominator <= 0.0) 0.0 else weight / denominator
    }
}


