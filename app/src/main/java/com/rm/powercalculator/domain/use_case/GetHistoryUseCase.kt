package com.rm.powercalculator.domain.use_case

import com.rm.powercalculator.domain.repository.CalculationRepository

class GetHistoryUseCase(private val repository: CalculationRepository) {
    operator fun invoke() = repository.getCalculations()
}
