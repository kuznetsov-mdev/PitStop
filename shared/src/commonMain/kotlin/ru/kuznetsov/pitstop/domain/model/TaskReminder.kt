package ru.kuznetsov.pitstop.domain.model

/**
 * A task the user should be notified about, together with the car it belongs to.
 */
data class TaskReminder(
    val car: Car,
    val task: TaskWithStat,
)
