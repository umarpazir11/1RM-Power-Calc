package com.rm.powercalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_records")
data class HistoryRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val weight: Double,
    val reps: Int,
    val oneRepMax: Double,
    val timestamp: Long = System.currentTimeMillis()
)


