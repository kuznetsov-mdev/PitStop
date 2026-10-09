package ru.kuznetsov.pitstop.features.history

import ru.kuznetsov.pitstop.domain.model.DistanceUnit
import ru.kuznetsov.pitstop.domain.model.ServiceHistoryEntry

/**
 * State of the service history screen.
 * [entries] come newest first; [brand] is the car's name shown under the title.
 */
data class HistoryUiState(
    val isLoading: Boolean = true,
    val brand: String = "",
    val entries: List<ServiceHistoryEntry> = emptyList(),
    val distanceUnit: DistanceUnit = DistanceUnit.KILOMETERS,
)
