package com.rm.powercalculator.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CalculationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val exerciseName: String,
    val weight: Double,
    val reps: Int,
    val oneRepMax: Double,
    val timestamp: Long
)
