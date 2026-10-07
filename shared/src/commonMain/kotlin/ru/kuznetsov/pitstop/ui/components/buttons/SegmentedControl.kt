package ru.kuznetsov.pitstop.ui.components.buttons

import ru.kuznetsov.pitstop.ui.components.ComponentPreview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

/** Group of mutually-exclusive options — theme picker, unit picker (km/mi). */
@Composable
fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = PitStopTheme.colors
    val dimens = PitStopTheme.dimens
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(dimens.radiusM))
            .background(colors.surface2)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        options.forEachIndexed { index, label ->
            Segment(
                label = label,
                selected = index == selectedIndex,
                onClick = { onSelect(index) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun RowScope.Segment(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = PitStopTheme.colors
    val shape = RoundedCornerShape(PitStopTheme.dimens.radiusS)
    Box(
        modifier = modifier
            .then(
                if (selected) {
                    Modifier
                        .shadow(1.dp, shape, ambientColor = colors.ink.copy(alpha = 0.08f), spotColor = colors.ink.copy(alpha = 0.08f))
                        .clip(shape)
                        .background(colors.surface)
                } else {
                    Modifier.clip(shape)
                },
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            style = PitStopTheme.typography.sans.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
            color = if (selected) colors.ink else colors.inkMuted,
        )
    }
}

@Preview
@Composable
private fun SegmentedControlLightPreview() = ComponentPreview(darkTheme = false) {
    SegmentedControl(listOf("Светлая", "Тёмная", "Системная"), selectedIndex = 0, onSelect = {})
}

@Preview
@Composable
private fun SegmentedControlDarkPreview() = ComponentPreview(darkTheme = true) {
    SegmentedControl(listOf("Светлая", "Тёмная", "Системная"), selectedIndex = 0, onSelect = {})
}