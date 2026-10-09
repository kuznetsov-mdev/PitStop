package ru.kuznetsov.pitstop.features.settings

import ru.kuznetsov.pitstop.domain.model.Settings

/**
 * State of the settings screen.
 * [settings] is `null` only until the stored settings arrive.
 */
data class SettingsUiState(
    val settings: Settings? = null,
)
