package com.rm.powercalculator.domain

import com.rm.powercalculator.data.HistoryRecordEntity

class AddHistoryRecordUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(record: HistoryRecordEntity) {
        repository.addRecord(record)
    }
}


