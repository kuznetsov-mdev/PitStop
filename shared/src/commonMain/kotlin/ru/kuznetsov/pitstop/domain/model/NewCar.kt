package ru.kuznetsov.pitstop.domain.model

/**
 * A car as entered in the add-car form, before it is stored and gets an id.
 */
data class NewCar(
    val brand: String,
    val mileageKm: Int,
    val vin: String?,
    val tasks: List<NewMaintenanceTask>,
)
