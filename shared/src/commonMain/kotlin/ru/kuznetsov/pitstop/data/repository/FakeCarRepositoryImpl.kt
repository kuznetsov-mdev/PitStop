package ru.kuznetsov.pitstop.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import ru.kuznetsov.pitstop.domain.model.Car
import ru.kuznetsov.pitstop.domain.model.MaintenanceTask
import ru.kuznetsov.pitstop.domain.model.NewCar
import ru.kuznetsov.pitstop.domain.model.NewMaintenanceTask
import ru.kuznetsov.pitstop.domain.repository.CarRepository

/**
 * In-memory [CarRepository] seeded from [FakeSeed].
 * Data lives only as long as the process; it is replaced by the SQLDelight implementation later.
 */
class FakeCarRepositoryImpl : CarRepository {
    private val cars = MutableStateFlow(FakeSeed.cars)
    private var nextId = 1

    override fun observeCars(): Flow<List<Car>> = cars

    override fun observeCarById(carId: String): Flow<Car?> =
        cars.map { list -> list.firstOrNull { it.id == carId } }

    override suspend fun addCar(car: NewCar): String {
        val carId = "car-${nextId++}"
        val stored = Car(
            id = carId,
            brand = car.brand,
            mileageKm = car.mileageKm,
            vin = car.vin,
            tasks = car.tasks.map(::toStoredTask),
        )
        cars.update { it + stored }
        return carId
    }

    override suspend fun deleteCar(carId: String) {
        cars.update { list -> list.filterNot { it.id == carId } }
    }

    override suspend fun updateMileage(carId: String, mileageKm: Int) {
        updateCar(carId) { it.copy(mileageKm = mileageKm) }
    }

    override suspend fun markTaskDone(carId: String, taskId: String, date: LocalDate) {
        updateCar(carId) { car ->
            car.copy(
                tasks = car.tasks.map { task ->
                    if (task.id != taskId) {
                        task
                    } else {
                        task.copy(
                            lastDoneKm = car.mileageKm,
                            lastDoneDate = if (task.intervalMonths != null) date else task.lastDoneDate,
                        )
                    }
                },
            )
        }
    }

    override suspend fun updateCar(carId: String, brand: String, vin: String?) {
        updateCar(carId) { it.copy(brand = brand, vin = vin) }
    }

    override suspend fun addTask(carId: String, task: NewMaintenanceTask) {
        updateCar(carId) { it.copy(tasks = it.tasks + toStoredTask(task)) }
    }

    override suspend fun updateTask(carId: String, task: MaintenanceTask) {
        updateCar(carId) { car ->
            car.copy(tasks = car.tasks.map { if (it.id == task.id) task else it })
        }
    }

    override suspend fun deleteTask(carId: String, taskId: String) {
        updateCar(carId) { car ->
            car.copy(tasks = car.tasks.filterNot { it.id == taskId })
        }
    }

    private fun updateCar(carId: String, transform: (Car) -> Car) {
        cars.update { list -> list.map { if (it.id == carId) transform(it) else it } }
    }

    private fun toStoredTask(task: NewMaintenanceTask) = MaintenanceTask(
        id = "task-${nextId++}",
        name = task.name,
        intervalKm = task.intervalKm,
        lastDoneKm = task.lastDoneKm,
        reminderWindowKm = task.reminderWindowKm ?: 0,
        intervalMonths = task.intervalMonths,
        lastDoneDate = task.lastDoneDate,
    )
}
