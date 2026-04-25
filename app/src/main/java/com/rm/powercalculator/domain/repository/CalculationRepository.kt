package com.rm.powercalculator.domain.repository

import com.rm.powercalculator.domain.model.Calculation
import kotlinx.coroutines.flow.Flow

interface CalculationRepository {

    suspend fun insertCalculation(calculation: Calculation)

    fun getCalculations(): Flow<List<Calculation>>

    suspend fun deleteCalculation(calculation: Calculation)

}
