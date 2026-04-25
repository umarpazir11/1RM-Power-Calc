package com.rm.powercalculator.domain.use_case

import com.rm.powercalculator.domain.model.Calculation
import com.rm.powercalculator.domain.repository.CalculationRepository

class SaveHistoryUseCase(private val repository: CalculationRepository) {
    suspend operator fun invoke(calculation: Calculation) {
        repository.insertCalculation(calculation)
    }
}
