package ru.kuznetsov.pitstop.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kuznetsov.pitstop.domain.model.Settings
import ru.kuznetsov.pitstop.domain.repository.SettingsRepository

/**
 * Emits the user's settings and re-emits them on every change.
 */
class ObserveSettingsUseCase(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): Flow<Settings> = settingsRepository.observeSettings()
}
