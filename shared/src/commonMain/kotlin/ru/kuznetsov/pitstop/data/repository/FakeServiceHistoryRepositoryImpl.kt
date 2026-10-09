package ru.kuznetsov.pitstop.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import ru.kuznetsov.pitstop.domain.model.NewServiceHistoryEntry
import ru.kuznetsov.pitstop.domain.model.ServiceHistoryEntry
import ru.kuznetsov.pitstop.domain.repository.ServiceHistoryRepository

/**
 * In-memory [ServiceHistoryRepository] seeded from [FakeSeed].
 * Data lives only as long as the process; it is replaced by the SQLDelight implementation later.
 */
class FakeServiceHistoryRepositoryImpl : ServiceHistoryRepository {
    private val entries = MutableStateFlow(FakeSeed.history)
    private var nextId = 1

    override fun observeHistory(carId: String): Flow<List<ServiceHistoryEntry>> =
        entries.map { list -> list.filter { it.carId == carId } }

    override suspend fun addEntry(entry: NewServiceHistoryEntry) {
        val stored = ServiceHistoryEntry(
            id = "entry-${nextId++}",
            carId = entry.carId,
            taskName = entry.taskName,
            date = entry.date,
            mileageKm = entry.mileageKm,
            costRub = entry.costRub,
            serviceName = entry.serviceName,
        )
        entries.update { it + stored }
    }

    override suspend fun updateEntry(entry: ServiceHistoryEntry) {
        entries.update { list -> list.map { if (it.id == entry.id) entry else it } }
    }

    override suspend fun deleteHistory(carId: String) {
        entries.update { list -> list.filterNot { it.carId == carId } }
    }
}
