package ru.kuznetsov.pitstop.domain.usecase

import ru.kuznetsov.pitstop.domain.model.MaintenanceTask

/**
 * Edits a maintenance task of an existing car.
 * Not specified yet: the method is a placeholder.
 */
class UpdateTaskUseCase {
    suspend operator fun invoke(carId: String, task: MaintenanceTask) {
    }
}
