package ru.kuznetsov.pitstop.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalPitStopColors = staticCompositionLocalOf { LightPitStopColors }
private val LocalPitStopTypography = staticCompositionLocalOf { PitStopTypography() }
private val LocalPitStopDimens = staticCompositionLocalOf { PitStopDimens() }

/** Access point for PitStop's design tokens, mirrored after `MaterialTheme.colorScheme` etc. */
object PitStopTheme {
    val colors: PitStopColorScheme
        @Composable get() = LocalPitStopColors.current

    val typography: PitStopTypography
        @Composable get() = LocalPitStopTypography.current

    val dimens: PitStopDimens
        @Composable get() = LocalPitStopDimens.current
}

/**
 * Root theme wrapper. Follows the system theme by default; a `darkTheme` override will be
 * wired to the Settings screen's theme picker once it exists (step 6).
 */
@Composable
fun PitStopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val pitStopColors = if (darkTheme) DarkPitStopColors else LightPitStopColors
    val materialColorScheme = if (darkTheme) {
        darkColorScheme(
            primary = pitStopColors.accent,
            onPrimary = pitStopColors.onAccent,
            background = pitStopColors.bg,
            onBackground = pitStopColors.ink,
            surface = pitStopColors.surface,
            onSurface = pitStopColors.ink,
            surfaceVariant = pitStopColors.surface2,
            outline = pitStopColors.rule,
        )
    } else {
        lightColorScheme(
            primary = pitStopColors.accent,
            onPrimary = pitStopColors.onAccent,
            background = pitStopColors.bg,
            onBackground = pitStopColors.ink,
            surface = pitStopColors.surface,
            onSurface = pitStopColors.ink,
            surfaceVariant = pitStopColors.surface2,
            outline = pitStopColors.rule,
        )
    }

    CompositionLocalProvider(
        LocalPitStopColors provides pitStopColors,
        LocalPitStopTypography provides PitStopTypography(),
        LocalPitStopDimens provides PitStopDimens(),
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            content = content,
        )
    }
}