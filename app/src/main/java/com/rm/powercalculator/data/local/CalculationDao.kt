package com.rm.powercalculator.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calculation: CalculationEntity)

    @Query("SELECT * FROM calculationentity ORDER BY timestamp DESC")
    fun getCalculations(): Flow<List<CalculationEntity>>

    @Delete
    suspend fun deleteCalculation(calculation: CalculationEntity)

}
