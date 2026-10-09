package ru.kuznetsov.pitstop.features.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.kuznetsov.pitstop.domain.usecase.ObserveCarByIdUseCase
import ru.kuznetsov.pitstop.domain.usecase.ObserveHistoryUseCase
import ru.kuznetsov.pitstop.domain.usecase.ObserveSettingsUseCase

/**
 * Drives the service history screen of one car.
 * The list is read-only: records appear when a task is marked as done on the car screen.
 */
class HistoryViewModel(
    carId: String,
    observeCarById: ObserveCarByIdUseCase,
    observeHistory: ObserveHistoryUseCase,
    observeSettings: ObserveSettingsUseCase,
) : ViewModel() {

    val state: StateFlow<HistoryUiState> = combine(
        observeCarById(carId),
        observeHistory(carId),
        observeSettings(),
    ) { car, entries, settings ->
        HistoryUiState(
            isLoading = false,
            brand = car?.brand.orEmpty(),
            entries = entries,
            distanceUnit = settings.distanceUnit,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), HistoryUiState())

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
