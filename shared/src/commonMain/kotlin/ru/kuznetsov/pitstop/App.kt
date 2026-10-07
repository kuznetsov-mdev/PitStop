package ru.kuznetsov.pitstop

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.kuznetsov.pitstop.features.navigation.PitStopNavGraph
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

@Composable
@Preview
fun App() {
    PitStopTheme {
        PitStopNavGraph()
    }
}
