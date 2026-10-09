package ru.kuznetsov.pitstop.features.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pitstop.shared.generated.resources.Res
import pitstop.shared.generated.resources.cost_rub
import pitstop.shared.generated.resources.history_empty
import pitstop.shared.generated.resources.screen_history
import ru.kuznetsov.pitstop.domain.model.DistanceUnit
import ru.kuznetsov.pitstop.domain.model.ServiceHistoryEntry
import ru.kuznetsov.pitstop.ui.components.screen.ScreenHorizontalPadding
import ru.kuznetsov.pitstop.ui.components.ScreenPreview
import ru.kuznetsov.pitstop.ui.components.screen.ScreenTitle
import ru.kuznetsov.pitstop.ui.components.screen.ScreenTopBar
import ru.kuznetsov.pitstop.ui.format.distanceText
import ru.kuznetsov.pitstop.ui.format.grouped
import ru.kuznetsov.pitstop.ui.format.toDisplayText
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

@Composable
fun HistoryScreen(
    carId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = koinViewModel { parametersOf(carId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HistoryContent(state = state, onBack = onBack, modifier = modifier)
}

@Composable
internal fun HistoryContent(
    state: HistoryUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize()) {
        ScreenTopBar(onBack = onBack)
        ScreenTitle(
            title = stringResource(Res.string.screen_history),
            subtitle = state.brand,
            monoSubtitle = true,
        )
        if (state.entries.isEmpty() && !state.isLoading) {
            EmptyHistory(Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = ScreenHorizontalPadding,
                    end = ScreenHorizontalPadding,
                    top = 8.dp,
                    bottom = 18.dp,
                ),
            ) {
                itemsIndexed(state.entries, key = { _, entry -> entry.id }) { index, entry ->
                    if (index > 0) {
                        Box(Modifier.fillMaxWidth().height(1.dp).background(PitStopTheme.colors.rule))
                    }
                    HistoryRow(entry = entry, distanceUnit = state.distanceUnit)
                }
            }
        }
    }
}

@Composable
private fun HistoryRow(entry: ServiceHistoryEntry, distanceUnit: DistanceUnit) {
    val colors = PitStopTheme.colors
    val typography = PitStopTheme.typography
    Column(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                entry.taskName,
                style = typography.sans.copy(fontSize = 13.5.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold),
                color = colors.ink,
                modifier = Modifier.weight(1f).alignByBaseline(),
            )
            Text(
                entry.date.toDisplayText(),
                style = typography.mono.copy(fontSize = 11.sp, lineHeight = 15.sp, fontWeight = FontWeight.Normal),
                color = colors.inkFaint,
                modifier = Modifier.alignByBaseline(),
            )
        }
        val meta = listOfNotNull(
            distanceText(entry.mileageKm, distanceUnit),
            entry.costRub?.let { stringResource(Res.string.cost_rub, it.grouped()) },
            entry.serviceName,
        )
        Text(
            meta.joinToString("   "),
            style = typography.mono.copy(fontSize = 11.sp, lineHeight = 15.sp, fontWeight = FontWeight.Normal),
            color = colors.inkMuted,
        )
    }
}

@Composable
private fun EmptyHistory(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().padding(horizontal = 32.dp), contentAlignment = Alignment.Center) {
        Text(
            stringResource(Res.string.history_empty),
            style = PitStopTheme.typography.sans.copy(fontSize = 13.sp, lineHeight = 19.sp),
            color = PitStopTheme.colors.inkFaint,
            textAlign = TextAlign.Center,
        )
    }
}

private val previewState = HistoryUiState(
    isLoading = false,
    brand = "Kia Rio",
    entries = listOf(
        ServiceHistoryEntry("h4", "c1", "Замена масла и фильтра", LocalDate(2026, 1, 15), 78_000, 2_400, "АвтоСервис Юг"),
        ServiceHistoryEntry("h3", "c1", "Воздушный фильтр", LocalDate(2025, 9, 10), 70_000, 900, "АвтоСервис Юг"),
        ServiceHistoryEntry("h2", "c1", "Тормозные колодки (передние)", LocalDate(2025, 6, 2), 60_000, null, null),
    ),
)

@Preview
@Composable
private fun HistoryContentLightPreview() = ScreenPreview(darkTheme = false) {
    HistoryContent(state = previewState, onBack = {})
}

@Preview
@Composable
private fun HistoryContentDarkPreview() = ScreenPreview(darkTheme = true) {
    HistoryContent(state = previewState, onBack = {})
}
