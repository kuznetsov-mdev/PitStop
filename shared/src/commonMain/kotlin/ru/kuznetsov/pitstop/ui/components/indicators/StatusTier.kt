package ru.kuznetsov.pitstop.ui.components.indicators

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

/** Maintenance status tier as the ui-kit sees it — drives the color and label of every status indicator. */
enum class StatusTier { Ok, Soon, Due }

val StatusTier.color: Color
    @Composable get() = when (this) {
        StatusTier.Ok -> PitStopTheme.colors.tierOk
        StatusTier.Soon -> PitStopTheme.colors.tierSoon
        StatusTier.Due -> PitStopTheme.colors.tierDue
    }

val StatusTier.label: String
    get() = when (this) {
        StatusTier.Ok -> "В порядке"
        StatusTier.Soon -> "Скоро"
        StatusTier.Due -> "Просрочено"
    }
