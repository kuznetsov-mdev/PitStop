package ru.kuznetsov.pitstop.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import pitstop.shared.generated.resources.Res
import pitstop.shared.generated.resources.screen_settings
import ru.kuznetsov.pitstop.features.navigation.PlaceholderScreen
import ru.kuznetsov.pitstop.ui.components.ComponentPreview

/** "Settings" tab — stub until step 6. */
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(title = stringResource(Res.string.screen_settings), modifier = modifier)
}

@Preview
@Composable
private fun SettingsScreenLightPreview() = ComponentPreview(darkTheme = false) {
    SettingsScreen()
}

@Preview
@Composable
private fun SettingsScreenDarkPreview() = ComponentPreview(darkTheme = true) {
    SettingsScreen()
}
