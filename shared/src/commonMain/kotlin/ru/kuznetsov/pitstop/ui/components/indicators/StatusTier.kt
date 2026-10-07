package ru.kuznetsov.pitstop.ui.components.indicators

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import pitstop.shared.generated.resources.Res
import pitstop.shared.generated.resources.status_due
import pitstop.shared.generated.resources.status_ok
import pitstop.shared.generated.resources.status_soon
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
    @Composable get() = when (this) {
        StatusTier.Ok -> stringResource(Res.string.status_ok)
        StatusTier.Soon -> stringResource(Res.string.status_soon)
        StatusTier.Due -> stringResource(Res.string.status_due)
    }
