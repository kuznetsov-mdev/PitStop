package ru.kuznetsov.pitstop.ui.components.buttons

import ru.kuznetsov.pitstop.ui.components.ComponentPreview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

/** Full-width-capable primary action button — accent background, used for form submits (e.g. "Сохранить авто"). */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = PitStopTheme.colors
    val dimens = PitStopTheme.dimens
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimens.radiusL))
            .background(colors.accent)
            .alpha(if (enabled) 1f else 0.5f)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(PaddingValues(horizontal = 22.dp, vertical = 15.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text,
            style = PitStopTheme.typography.sans.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
            color = colors.onAccent,
        )
    }
}

@Preview
@Composable
private fun PrimaryButtonLightPreview() = ComponentPreview(darkTheme = false) {
    PrimaryButton("Сохранить авто", onClick = {})
}

@Preview
@Composable
private fun PrimaryButtonDarkPreview() = ComponentPreview(darkTheme = true) {
    PrimaryButton("Сохранить авто", onClick = {})
}
