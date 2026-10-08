package ru.kuznetsov.pitstop.domain.model

import kotlinx.datetime.LocalDate

/**
 * One completed job in a car's service log.
 * Keeps the task name as text so the record survives edits to the task itself.
 */
data class ServiceHistoryEntry(
    val id: String,
    val carId: String,
    val taskName: String,
    val date: LocalDate,
    val mileageKm: Int,
    val costRub: Int? = null,
    val serviceName: String? = null,
)
