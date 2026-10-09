package ru.kuznetsov.pitstop.features.cardetail

import androidx.compose.ui.graphics.vector.ImageVector
import ru.kuznetsov.pitstop.ui.icons.PitStopIcons

private val oilKeywords = listOf("масл", "oil", "aceite")
private val brakeKeywords = listOf("колод", "тормоз", "brake", "pad", "freno", "pastilla")
private val filterKeywords = listOf("фильтр", "filter", "filtro")

internal fun taskIcon(taskName: String): ImageVector {
    val name = taskName.lowercase()
    return when {
        oilKeywords.any { it in name } -> PitStopIcons.Droplet
        brakeKeywords.any { it in name } -> PitStopIcons.Disc
        filterKeywords.any { it in name } -> PitStopIcons.Filter
        else -> PitStopIcons.Wrench
    }
}
