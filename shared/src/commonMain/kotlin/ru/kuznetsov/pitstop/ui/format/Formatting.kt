package ru.kuznetsov.pitstop.ui.format

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.number
import org.jetbrains.compose.resources.stringResource
import pitstop.shared.generated.resources.Res
import pitstop.shared.generated.resources.distance_mi
import pitstop.shared.generated.resources.mileage_km
import ru.kuznetsov.pitstop.domain.model.DistanceUnit
import kotlin.math.abs
import kotlin.math.roundToInt

private const val KM_PER_MILE = 1.609344
private const val GROUP_SEPARATOR = " "

fun Int.grouped(): String {
    val digits = abs(toLong()).toString().reversed().chunked(3).joinToString(GROUP_SEPARATOR).reversed()
    return if (this < 0) "-$digits" else digits
}

fun Int.kmIn(unit: DistanceUnit): Int = when (unit) {
    DistanceUnit.KILOMETERS -> this
    DistanceUnit.MILES -> (this / KM_PER_MILE).roundToInt()
}

@Composable
fun distanceText(km: Int, unit: DistanceUnit): String {
    val template = when (unit) {
        DistanceUnit.KILOMETERS -> Res.string.mileage_km
        DistanceUnit.MILES -> Res.string.distance_mi
    }
    return stringResource(template, km.kmIn(unit).grouped())
}

fun LocalDate.toDisplayText(): String = "${day.twoDigits()}.${month.number.twoDigits()}.$year"

fun LocalTime.toDisplayText(): String = "${hour.twoDigits()}:${minute.twoDigits()}"

fun parseDisplayDate(text: String): LocalDate? {
    val parts = text.trim().split('.').map { it.toIntOrNull() ?: return null }
    if (parts.size != 3) return null
    return try {
        LocalDate(parts[2], parts[1], parts[0])
    } catch (_: IllegalArgumentException) {
        null
    }
}

private fun Int.twoDigits(): String = toString().padStart(2, '0')
