package ru.kuznetsov.pitstop.features.cardetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kuznetsov.pitstop.domain.model.Car
import ru.kuznetsov.pitstop.domain.usecase.CarAlertsUseCase
import ru.kuznetsov.pitstop.domain.usecase.DeleteCarUseCase
import ru.kuznetsov.pitstop.domain.usecase.GetTodayUseCase
import ru.kuznetsov.pitstop.domain.usecase.MarkTaskDoneUseCase
import ru.kuznetsov.pitstop.domain.usecase.ObserveCarByIdUseCase
import ru.kuznetsov.pitstop.domain.usecase.ObserveSettingsUseCase
import ru.kuznetsov.pitstop.domain.usecase.SortedTaskStatsUseCase
import ru.kuznetsov.pitstop.domain.usecase.UpdateMileageUseCase

/**
 * Drives the car screen: the task list sorted by urgency, quick mileage editing,
 * marking a task as done and deleting the car after a confirmation.
 * The mileage is always entered in kilometers, whatever unit is used for display.
 */
class CarDetailViewModel(
    private val carId: String,
    observeCarById: ObserveCarByIdUseCase,
    observeSettings: ObserveSettingsUseCase,
    private val sortedTaskStats: SortedTaskStatsUseCase,
    private val carAlerts: CarAlertsUseCase,
    private val getToday: GetTodayUseCase,
    private val updateMileage: UpdateMileageUseCase,
    private val markTaskDone: MarkTaskDoneUseCase,
    private val deleteCar: DeleteCarUseCase,
) : ViewModel() {

    private val isEditingMileage = MutableStateFlow(false)
    private val isConfirmingDelete = MutableStateFlow(false)

    val state: StateFlow<CarDetailUiState> = combine(
        observeCarById(carId),
        observeSettings(),
        isEditingMileage,
        isConfirmingDelete,
    ) { car, settings, editing, confirming ->
        CarDetailUiState(
            isLoading = false,
            car = car?.toDetails(),
            distanceUnit = settings.distanceUnit,
            isEditingMileage = editing,
            isConfirmingDelete = confirming,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), CarDetailUiState())

    fun onEditMileage() {
        isEditingMileage.value = true
    }

    fun onSaveMileage(text: String) {
        val mileageKm = text.trim().toIntOrNull()
        viewModelScope.launch {
            if (mileageKm != null) updateMileage(carId, mileageKm)
            isEditingMileage.value = false
        }
    }

    fun onMarkTaskDone(taskId: String) {
        viewModelScope.launch { markTaskDone(carId, taskId, getToday()) }
    }

    fun onDeleteRequest() {
        isConfirmingDelete.value = true
    }

    fun onDeleteDismiss() {
        isConfirmingDelete.value = false
    }

    fun onDeleteConfirm() {
        isConfirmingDelete.value = false
        viewModelScope.launch { deleteCar(carId) }
    }

    private fun Car.toDetails(): CarDetails {
        val today = getToday()
        val alerts = carAlerts(this, today)
        return CarDetails(
            brand = brand,
            vin = vin,
            mileageKm = mileageKm,
            alertCount = alerts.total,
            taskCount = alerts.taskCount,
            worstStatus = alerts.worstStatus,
            tasks = sortedTaskStats(this, today).map { (task, stat) ->
                TaskItem(
                    id = task.id,
                    name = task.name,
                    intervalKm = task.intervalKm,
                    remainingKm = stat.remainingKm,
                    remainingMonths = stat.remainingMonths,
                    status = stat.status,
                    progress = stat.progress,
                )
            },
        )
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
