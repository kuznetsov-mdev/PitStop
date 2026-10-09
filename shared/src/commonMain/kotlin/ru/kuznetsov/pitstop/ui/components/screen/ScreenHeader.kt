package ru.kuznetsov.pitstop.ui.components.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import pitstop.shared.generated.resources.Res
import pitstop.shared.generated.resources.action_back
import ru.kuznetsov.pitstop.ui.components.buttons.AppIconButton
import ru.kuznetsov.pitstop.ui.icons.PitStopIcons
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

internal val ScreenHorizontalPadding = 18.dp

@Composable
internal fun ScreenTopBar(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = ScreenHorizontalPadding, end = ScreenHorizontalPadding, top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            AppIconButton(
                icon = PitStopIcons.ChevronLeft,
                onClick = onBack,
                contentDescription = stringResource(Res.string.action_back),
            )
        } else {
            Spacer(Modifier.size(31.dp))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), content = actions)
    }
}

@Composable
internal fun ScreenTitle(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    monoSubtitle: Boolean = false,
) {
    val colors = PitStopTheme.colors
    val typography = PitStopTheme.typography
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = ScreenHorizontalPadding, end = ScreenHorizontalPadding, top = 11.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            title,
            style = typography.display.copy(fontSize = 20.sp, lineHeight = 26.sp),
            color = colors.ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (subtitle != null) {
            val subtitleStyle = if (monoSubtitle) {
                typography.mono.copy(fontSize = 11.5.sp, lineHeight = 15.sp, fontWeight = FontWeight.Normal)
            } else {
                typography.sans.copy(fontSize = 11.5.sp, lineHeight = 15.sp)
            }
            Text(subtitle, style = subtitleStyle, color = colors.inkFaint)
        }
    }
}
