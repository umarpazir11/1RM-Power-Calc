package com.rm.powercalculator.data

import com.rm.powercalculator.domain.HistoryRepository
import kotlinx.coroutines.flow.Flow

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao
) : HistoryRepository {
    
    override suspend fun addRecord(record: HistoryRecordEntity) {
        historyDao.insertRecord(record)
    }
    
    override suspend fun deleteRecord(record: HistoryRecordEntity) {
        historyDao.deleteRecord(record)
    }
    
    override fun getHistory(): Flow<List<HistoryRecordEntity>> {
        return historyDao.getHistory()
    }
}
