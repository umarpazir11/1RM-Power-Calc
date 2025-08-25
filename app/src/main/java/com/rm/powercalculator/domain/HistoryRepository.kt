package com.rm.powercalculator.domain

import com.rm.powercalculator.data.HistoryRecordEntity
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun addRecord(record: HistoryRecordEntity)
    suspend fun deleteRecord(record: HistoryRecordEntity)
    fun getHistory(): Flow<List<HistoryRecordEntity>>
}
