package com.rm.powercalculator.data.repository

import com.rm.powercalculator.data.local.CalculationDao
import com.rm.powercalculator.data.local.CalculationEntity
import com.rm.powercalculator.domain.model.Calculation
import com.rm.powercalculator.domain.repository.CalculationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculationRepositoryImpl(private val dao: CalculationDao) : CalculationRepository {

    override suspend fun insertCalculation(calculation: Calculation) {
        dao.insertCalculation(calculation.toEntity())
    }

    override fun getCalculations(): Flow<List<Calculation>> {
        return dao.getCalculations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteCalculation(calculation: Calculation) {
        dao.deleteCalculation(calculation.toEntity())
    }
}

private fun Calculation.toEntity(): CalculationEntity {
    return CalculationEntity(
        id = id,
        exerciseName = exerciseName,
        weight = weight,
        reps = reps,
        oneRepMax = oneRepMax,
        timestamp = timestamp
    )
}

private fun CalculationEntity.toDomain(): Calculation {
    return Calculation(
        id = id,
        exerciseName = exerciseName,
        weight = weight,
        reps = reps,
        oneRepMax = oneRepMax,
        timestamp = timestamp
    )
}
