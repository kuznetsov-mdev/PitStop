package ru.kuznetsov.pitstop.domain.usecase

import ru.kuznetsov.pitstop.domain.model.NewMaintenanceTask

/**
 * Adds a maintenance task to an existing car.
 * Not specified yet: the method is a placeholder.
 */
class AddTaskUseCase {
    suspend operator fun invoke(carId: String, task: NewMaintenanceTask) {
    }
}
