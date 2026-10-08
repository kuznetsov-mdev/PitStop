package ru.kuznetsov.pitstop.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kuznetsov.pitstop.domain.model.Car
import ru.kuznetsov.pitstop.domain.repository.CarRepository

/**
 * Emits the list of all cars and re-emits it on every change.
 */
class ObserveCarsUseCase(
    private val carRepository: CarRepository,
) {
    operator fun invoke(): Flow<List<Car>> = carRepository.observeCars()
}
