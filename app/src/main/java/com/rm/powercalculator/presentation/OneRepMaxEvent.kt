package com.rm.powercalculator.presentation

import com.rm.powercalculator.data.HistoryRecordEntity

sealed class OneRepMaxEvent {
    data class OnWeightChange(val weight: String) : OneRepMaxEvent()
    data class OnRepsChange(val reps: String) : OneRepMaxEvent()
    object OnCalculateClick : OneRepMaxEvent()
    data class OnDeleteHistoryRecord(val record: HistoryRecordEntity) : OneRepMaxEvent()
    data class OnUndoDeleteHistoryRecord(val record: HistoryRecordEntity) : OneRepMaxEvent()
}
