package com.rm.powercalculator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CalculationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CalculationDatabase : RoomDatabase() {
    abstract fun calculationDao(): CalculationDao
}
