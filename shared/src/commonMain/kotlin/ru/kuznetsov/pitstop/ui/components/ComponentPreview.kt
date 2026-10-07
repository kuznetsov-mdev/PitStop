package ru.kuznetsov.pitstop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

/** Shared wrapper for every component's light/dark `@Preview` pair — applies the theme and a background/padding box so swatches aren't rendered edge-to-edge. */
@Composable
internal fun ComponentPreview(darkTheme: Boolean, content: @Composable () -> Unit) {
    PitStopTheme(darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .background(PitStopTheme.colors.bg)
                .padding(16.dp),
        ) {
            content()
        }
    }
}