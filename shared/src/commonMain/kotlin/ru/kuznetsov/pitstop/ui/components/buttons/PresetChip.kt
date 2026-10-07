package ru.kuznetsov.pitstop.ui.components.buttons

import ru.kuznetsov.pitstop.ui.components.ComponentPreview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

/** Dashed pill used to quick-add a preset maintenance task (e.g. "+ Масло") when adding a car. */
@Composable
fun PresetChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = PitStopTheme.colors
    val shape = RoundedCornerShape(50)
    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.surface2)
            .drawBehind {
                val strokeWidthPx = 1.dp.toPx()
                drawRoundRect(
                    color = colors.rule,
                    cornerRadius = CornerRadius(size.minDimension / 2f),
                    style = Stroke(
                        width = strokeWidthPx,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 3.dp.toPx())),
                    ),
                )
            }
            .clickable(onClick = onClick)
            .padding(horizontal = 13.dp, vertical = 7.dp),
    ) {
        Text(
            "+ $label",
            style = PitStopTheme.typography.sans.copy(fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold),
            color = colors.inkMuted,
        )
    }
}

@Preview
@Composable
private fun PresetChipLightPreview() = ComponentPreview(darkTheme = false) {
    PresetChip("Масло", onClick = {})
}

@Preview
@Composable
private fun PresetChipDarkPreview() = ComponentPreview(darkTheme = true) {
    PresetChip("Масло", onClick = {})
}