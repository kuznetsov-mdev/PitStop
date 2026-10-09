package ru.kuznetsov.pitstop.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.kuznetsov.pitstop.domain.model.Settings
import ru.kuznetsov.pitstop.domain.repository.SettingsRepository

/**
 * In-memory [SettingsRepository] seeded from [FakeSeed].
 * Settings reset to the defaults on every app start until persistence is added.
 */
class FakeSettingsRepositoryImpl : SettingsRepository {
    private val settings = MutableStateFlow(FakeSeed.settings)

    override fun observeSettings(): Flow<Settings> = settings

    override suspend fun updateSettings(settings: Settings) {
        this.settings.value = settings
    }
}
