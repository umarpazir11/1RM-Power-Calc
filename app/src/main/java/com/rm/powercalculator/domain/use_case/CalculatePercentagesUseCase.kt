package com.rm.powercalculator.domain.use_case

import com.rm.powercalculator.domain.model.PercentageBasedCalculation

class CalculatePercentagesUseCase {
    operator fun invoke(oneRepMax: Double): List<PercentageBasedCalculation> {
        val percentages = listOf(95, 90, 85, 80, 75, 70)
        return percentages.map {
            PercentageBasedCalculation(
                percentage = it,
                weight = oneRepMax * (it / 100.0)
            )
        }
    }
}
