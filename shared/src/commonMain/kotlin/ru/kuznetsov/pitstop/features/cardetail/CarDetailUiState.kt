package ru.kuznetsov.pitstop.features.cardetail

import ru.kuznetsov.pitstop.domain.model.DistanceUnit
import ru.kuznetsov.pitstop.domain.model.TaskStatus

/**
 * State of the car screen.
 * [car] is `null` while loading and after the car has been deleted; [isMissing] tells the two apart
 * and is the signal for the screen to close itself.
 */
data class CarDetailUiState(
    val isLoading: Boolean = true,
    val car: CarDetails? = null,
    val distanceUnit: DistanceUnit = DistanceUnit.KILOMETERS,
    val isEditingMileage: Boolean = false,
    val isConfirmingDelete: Boolean = false,
) {
    val isMissing: Boolean get() = !isLoading && car == null
}

/**
 * The car as shown in the screen header and its tasks, most urgent first.
 * [alertCount] of [taskCount] tasks are soon or due; [worstStatus] colors that counter.
 */
data class CarDetails(
    val brand: String,
    val vin: String?,
    val mileageKm: Int,
    val alertCount: Int,
    val taskCount: Int,
    val worstStatus: TaskStatus,
    val tasks: List<TaskItem>,
)

/**
 * One maintenance task row.
 * [remainingKm] is negative once overdue; [remainingMonths] is `null` for tasks without a time interval.
 */
data class TaskItem(
    val id: String,
    val name: String,
    val intervalKm: Int,
    val remainingKm: Int,
    val remainingMonths: Int?,
    val status: TaskStatus,
    val progress: Float,
)
