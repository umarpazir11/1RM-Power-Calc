package com.rm.powercalculator.domain

import com.rm.powercalculator.data.HistoryRecordEntity
import kotlinx.coroutines.flow.Flow

class GetHistoryUseCase(
    private val repository: HistoryRepository
) {
    operator fun invoke(): Flow<List<HistoryRecordEntity>> {
        return repository.getHistory()
    }
}

