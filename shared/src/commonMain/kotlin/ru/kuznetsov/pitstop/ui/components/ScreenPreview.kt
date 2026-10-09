package ru.kuznetsov.pitstop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

@Composable
internal fun ScreenPreview(darkTheme: Boolean, content: @Composable () -> Unit) {
    PitStopTheme(darkTheme = darkTheme) {
        Box(Modifier.background(PitStopTheme.colors.bg)) {
            content()
        }
    }
}
