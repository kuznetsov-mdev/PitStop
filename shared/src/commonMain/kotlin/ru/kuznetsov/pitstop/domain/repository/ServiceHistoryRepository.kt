package ru.kuznetsov.pitstop.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.kuznetsov.pitstop.domain.model.NewServiceHistoryEntry
import ru.kuznetsov.pitstop.domain.model.ServiceHistoryEntry

/**
 * Service log of completed jobs, kept per car.
 * [addEntry] assigns the record its id.
 */
interface ServiceHistoryRepository {
    fun observeHistory(carId: String): Flow<List<ServiceHistoryEntry>>

    suspend fun addEntry(entry: NewServiceHistoryEntry)

    suspend fun updateEntry(entry: ServiceHistoryEntry)

    suspend fun deleteHistory(carId: String)
}
