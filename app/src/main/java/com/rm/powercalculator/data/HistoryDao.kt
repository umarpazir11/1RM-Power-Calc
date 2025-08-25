package com.rm.powercalculator.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    
    @Insert
    suspend fun insertRecord(record: HistoryRecordEntity)
    
    @Delete
    suspend fun deleteRecord(record: HistoryRecordEntity)
    
    @Query("SELECT * FROM history_records ORDER BY timestamp DESC LIMIT 500")
    fun getHistory(): Flow<List<HistoryRecordEntity>>
}
