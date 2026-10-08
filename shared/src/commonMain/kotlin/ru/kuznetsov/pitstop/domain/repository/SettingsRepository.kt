package ru.kuznetsov.pitstop.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.kuznetsov.pitstop.domain.model.Settings

/**
 * Storage of user preferences.
 * [updateSettings] replaces the stored value as a whole.
 */
interface SettingsRepository {
    fun observeSettings(): Flow<Settings>

    suspend fun updateSettings(settings: Settings)
}
