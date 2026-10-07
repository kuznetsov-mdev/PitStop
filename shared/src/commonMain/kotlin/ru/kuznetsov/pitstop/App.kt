package ru.kuznetsov.pitstop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kuznetsov.pitstop.ui.icons.PitStopIcons
import ru.kuznetsov.pitstop.ui.theme.PitStopColorScheme
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

@Composable
@Preview
fun App() {
    PitStopTheme {
        ThemeShowcase()
    }
}

/**
 * Temporary verification screen for step 1 (theme & typography) — shows every color token
 * and font role so both light and dark themes can be eyeballed before any real screen exists.
 * Will be replaced by the navigation graph once screens land (step 3+).
 */
@Composable
private fun ThemeShowcase() {
    val colors = PitStopTheme.colors
    val typography = PitStopTheme.typography
    val dimens = PitStopTheme.dimens

    Column(
        modifier = Modifier
            .background(colors.bg)
            .safeContentPadding()
            .fillMaxSize()
            .padding(dimens.spaceL),
        verticalArrangement = Arrangement.spacedBy(dimens.spaceL),
    ) {
        Text("PitStop", style = typography.display, color = colors.ink)
        Text(
            "Учёт регламентного ТО по пробегу и по времени",
            style = typography.sans,
            color = colors.inkMuted,
        )
        Text("84 300 КМ · 09:00", style = typography.mono, color = colors.ink)

        Row(horizontalArrangement = Arrangement.spacedBy(dimens.spaceS)) {
            ColorSwatch("bg", colors.bg, colors)
            ColorSwatch("surface", colors.surface, colors)
            ColorSwatch("surface2", colors.surface2, colors)
            ColorSwatch("surface3", colors.surface3, colors)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(dimens.spaceS)) {
            ColorSwatch("ink", colors.ink, colors)
            ColorSwatch("inkMuted", colors.inkMuted, colors)
            ColorSwatch("inkFaint", colors.inkFaint, colors)
            ColorSwatch("rule", colors.rule, colors)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(dimens.spaceS)) {
            ColorSwatch("accent", colors.accent, colors)
            ColorSwatch("tierOk", colors.tierOk, colors)
            ColorSwatch("tierSoon", colors.tierSoon, colors)
            ColorSwatch("tierDue", colors.tierDue, colors)
        }

        val icons = listOf(
            "car" to PitStopIcons.Car,
            "gauge" to PitStopIcons.Gauge,
            "droplet" to PitStopIcons.Droplet,
            "disc" to PitStopIcons.Disc,
            "filter" to PitStopIcons.Filter,
            "wrench" to PitStopIcons.Wrench,
            "gear" to PitStopIcons.Gear,
            "clock" to PitStopIcons.Clock,
            "check" to PitStopIcons.Check,
            "chevronLeft" to PitStopIcons.ChevronLeft,
            "pencil" to PitStopIcons.Pencil,
            "trash" to PitStopIcons.Trash,
            "bell" to PitStopIcons.Bell,
            "plus" to PitStopIcons.Plus,
            "minus" to PitStopIcons.Minus,
            "sun" to PitStopIcons.Sun,
            "moonToggle" to PitStopIcons.MoonToggle,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            horizontalArrangement = Arrangement.spacedBy(dimens.spaceS),
            verticalArrangement = Arrangement.spacedBy(dimens.spaceS),
        ) {
            items(icons) { (label, icon) -> IconSwatch(label, icon, colors) }
        }
    }
}

@Composable
private fun IconSwatch(label: String, icon: ImageVector, colors: PitStopColorScheme) {
    Column {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(colors.surface2, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = label, tint = colors.ink, modifier = Modifier.size(18.dp))
        }
        Text(
            label,
            style = PitStopTheme.typography.mono.copy(fontSize = 8.sp),
            color = colors.inkFaint,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ColorSwatch(label: String, color: Color, colors: PitStopColorScheme) {
    Column {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(color, RoundedCornerShape(10.dp)),
        )
        Text(
            label,
            style = PitStopTheme.typography.mono.copy(fontSize = 9.sp),
            color = colors.inkFaint,
            textAlign = TextAlign.Center,
        )
    }
}