package ru.kuznetsov.pitstop.ui.format

import ru.kuznetsov.pitstop.domain.model.TaskStatus
import ru.kuznetsov.pitstop.ui.components.indicators.StatusTier

fun TaskStatus.toTier(): StatusTier = when (this) {
    TaskStatus.OK -> StatusTier.Ok
    TaskStatus.SOON -> StatusTier.Soon
    TaskStatus.DUE -> StatusTier.Due
}
