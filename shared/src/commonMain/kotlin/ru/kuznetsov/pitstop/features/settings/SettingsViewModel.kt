package ru.kuznetsov.pitstop.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kuznetsov.pitstop.domain.model.DistanceUnit
import ru.kuznetsov.pitstop.domain.model.Settings
import ru.kuznetsov.pitstop.domain.model.ThemeMode
import ru.kuznetsov.pitstop.domain.usecase.ObserveSettingsUseCase
import ru.kuznetsov.pitstop.domain.usecase.UpdateSettingsUseCase

/**
 * Drives the settings screen: every change is stored right away, there is no save button.
 * The reminder window is edited in thousands of kilometers and stored in kilometers.
 */
class SettingsViewModel(
    observeSettings: ObserveSettingsUseCase,
    private val updateSettings: UpdateSettingsUseCase,
) : ViewModel() {

    val state: StateFlow<SettingsUiState> = observeSettings()
        .map { SettingsUiState(settings = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), SettingsUiState())

    fun onThemeSelect(themeMode: ThemeMode) = update { it.copy(themeMode = themeMode) }

    fun onUnitSelect(distanceUnit: DistanceUnit) = update { it.copy(distanceUnit = distanceUnit) }

    fun onPushToggle(enabled: Boolean) = update { it.copy(pushEnabled = enabled) }

    fun onReminderWindowChange(thousandKm: Int) =
        update { it.copy(defaultReminderWindowKm = thousandKm * KM_PER_STEP) }

    private fun update(change: (Settings) -> Settings) {
        val current = state.value.settings ?: return
        viewModelScope.launch { updateSettings(change(current)) }
    }

    companion object {
        const val KM_PER_STEP = 1_000
        private const val STOP_TIMEOUT_MS = 5_000L
    }
}
