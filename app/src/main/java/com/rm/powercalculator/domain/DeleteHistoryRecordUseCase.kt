package com.rm.powercalculator.domain

import com.rm.powercalculator.data.HistoryRecordEntity

class DeleteHistoryRecordUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(record: HistoryRecordEntity) {
        repository.deleteRecord(record)
    }
}


