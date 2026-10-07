package ru.kuznetsov.pitstop.features.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import pitstop.shared.generated.resources.Res
import pitstop.shared.generated.resources.action_back
import pitstop.shared.generated.resources.screen_car_detail
import pitstop.shared.generated.resources.screen_history
import ru.kuznetsov.pitstop.ui.components.ComponentPreview
import ru.kuznetsov.pitstop.ui.components.buttons.AppIconButton
import ru.kuznetsov.pitstop.ui.components.buttons.PrimaryButton
import ru.kuznetsov.pitstop.ui.icons.PitStopIcons
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

/** Temporary stand-in for a real screen (step 6) — title, optional back button and the outgoing transitions as buttons, so the graph can be walked end to end. */
@Composable
internal fun PlaceholderScreen(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable ColumnScope.() -> Unit = {},
) {
    val colors = PitStopTheme.colors
    val typography = PitStopTheme.typography
    val dimens = PitStopTheme.dimens
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimens.spaceL),
        verticalArrangement = Arrangement.spacedBy(dimens.spaceM),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceM),
        ) {
            if (onBack != null) {
                AppIconButton(
                    icon = PitStopIcons.ChevronLeft,
                    onClick = onBack,
                    contentDescription = stringResource(Res.string.action_back),
                )
            }
            Text(title, style = typography.display.copy(fontSize = 22.sp, lineHeight = 28.sp), color = colors.ink)
        }
        if (subtitle != null) {
            Text(subtitle, style = typography.mono.copy(fontSize = 12.sp), color = colors.inkFaint)
        }
        actions()
    }
}

@Preview
@Composable
private fun PlaceholderScreenLightPreview() = ComponentPreview(darkTheme = false) {
    PlaceholderScreen(title = stringResource(Res.string.screen_car_detail), subtitle = "c1", onBack = {}) {
        PrimaryButton(text = stringResource(Res.string.screen_history), onClick = {}, modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun PlaceholderScreenDarkPreview() = ComponentPreview(darkTheme = true) {
    PlaceholderScreen(title = stringResource(Res.string.screen_car_detail), subtitle = "c1", onBack = {}) {
        PrimaryButton(text = stringResource(Res.string.screen_history), onClick = {}, modifier = Modifier.fillMaxWidth())
    }
}
