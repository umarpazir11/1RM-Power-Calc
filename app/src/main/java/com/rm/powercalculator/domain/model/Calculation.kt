package com.rm.powercalculator.domain.model

data class Calculation(
    val id: Long = 0,
    val exerciseName: String,
    val weight: Double,
    val reps: Int,
    val oneRepMax: Double,
    val timestamp: Long
)
