package ru.kuznetsov.pitstop.domain.usecase

import ru.kuznetsov.pitstop.domain.model.NewCar
import ru.kuznetsov.pitstop.domain.model.NewMaintenanceTask
import ru.kuznetsov.pitstop.domain.repository.CarRepository
import kotlin.math.roundToInt

/**
 * Validates and stores a car from the add-car form.
 * Returns the new car's id, or `null` when the brand is blank or the mileage is not positive.
 * Tasks without a name or with a non-positive interval are dropped.
 * A task without a reminder window gets 15% of its interval; a last-done date is kept
 * only for tasks with a time interval; a blank VIN is stored as `null`.
 */
class AddCarUseCase(
    private val carRepository: CarRepository,
) {
    suspend operator fun invoke(car: NewCar): String? {
        val brand = car.brand.trim()
        if (brand.isEmpty() || car.mileageKm <= 0) return null
        return carRepository.addCar(
            car.copy(
                brand = brand,
                vin = car.vin?.trim()?.takeIf { it.isNotEmpty() },
                tasks = car.tasks.mapNotNull(::normalize),
            ),
        )
    }

    private fun normalize(task: NewMaintenanceTask): NewMaintenanceTask? {
        val name = task.name.trim()
        if (name.isEmpty() || task.intervalKm <= 0) return null
        val intervalMonths = task.intervalMonths?.takeIf { it > 0 }
        return task.copy(
            name = name,
            reminderWindowKm = task.reminderWindowKm ?: (task.intervalKm * DEFAULT_REMINDER_SHARE).roundToInt(),
            intervalMonths = intervalMonths,
            lastDoneDate = task.lastDoneDate.takeIf { intervalMonths != null },
        )
    }

    private companion object {
        const val DEFAULT_REMINDER_SHARE = 0.15
    }
}
