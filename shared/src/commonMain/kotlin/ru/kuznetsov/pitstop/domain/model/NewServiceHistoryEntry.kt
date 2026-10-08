package ru.kuznetsov.pitstop.domain.model

import kotlinx.datetime.LocalDate

/**
 * A service log record before it is stored and gets an id.
 */
data class NewServiceHistoryEntry(
    val carId: String,
    val taskName: String,
    val date: LocalDate,
    val mileageKm: Int,
    val costRub: Int? = null,
    val serviceName: String? = null,
)
