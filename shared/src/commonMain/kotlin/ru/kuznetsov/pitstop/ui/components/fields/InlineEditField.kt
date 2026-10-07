package ru.kuznetsov.pitstop.ui.components.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kuznetsov.pitstop.ui.components.ComponentPreview
import ru.kuznetsov.pitstop.ui.icons.PitStopIcons
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

/** Compact numeric field + accent confirm button — quick mileage edit directly on the car screen, no form. */
@Composable
fun InlineEditField(
    initialValue: String,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = PitStopTheme.colors
    var text by remember(initialValue) { mutableStateOf(initialValue) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FieldBox(
            value = text,
            onValueChange = { text = it },
            textStyle = PitStopTheme.typography.mono.copy(fontSize = 13.sp),
            shape = RoundedCornerShape(PitStopTheme.dimens.radiusS),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 7.dp),
            modifier = Modifier.weight(1f),
            keyboardType = KeyboardType.Number,
        )
        Box(
            modifier = Modifier
                .size(27.dp)
                .clip(RoundedCornerShape(PitStopTheme.dimens.radiusS))
                .background(colors.accent)
                .clickable { onSave(text) },
            contentAlignment = Alignment.Center,
        ) {
            Icon(PitStopIcons.Check, contentDescription = "Сохранить", tint = colors.onAccent, modifier = Modifier.size(14.dp))
        }
    }
}

@Preview
@Composable
private fun InlineEditFieldLightPreview() = ComponentPreview(darkTheme = false) {
    InlineEditField(initialValue = "84300", onSave = {})
}

@Preview
@Composable
private fun InlineEditFieldDarkPreview() = ComponentPreview(darkTheme = true) {
    InlineEditField(initialValue = "84300", onSave = {})
}
