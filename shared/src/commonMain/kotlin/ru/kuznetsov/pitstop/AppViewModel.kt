package ru.kuznetsov.pitstop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.kuznetsov.pitstop.domain.model.ThemeMode
import ru.kuznetsov.pitstop.domain.usecase.ObserveSettingsUseCase

/**
 * App-wide state above the navigation graph.
 * Exposes the theme chosen in settings, so switching it there repaints every screen at once.
 */
class AppViewModel(
    observeSettings: ObserveSettingsUseCase,
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = observeSettings()
        .map { it.themeMode }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), ThemeMode.SYSTEM)

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
