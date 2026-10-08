package ru.kuznetsov.pitstop.domain.usecase

import ru.kuznetsov.pitstop.domain.model.Settings
import ru.kuznetsov.pitstop.domain.repository.SettingsRepository

/**
 * Stores the user's settings.
 * The default reminder window is kept within 1 000..9 000 km, the range of the stepper on the settings screen.
 */
class UpdateSettingsUseCase(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(settings: Settings) {
        settingsRepository.updateSettings(
            settings.copy(
                defaultReminderWindowKm = settings.defaultReminderWindowKm
                    .coerceIn(MIN_REMINDER_WINDOW_KM, MAX_REMINDER_WINDOW_KM),
            ),
        )
    }

    private companion object {
        const val MIN_REMINDER_WINDOW_KM = 1_000
        const val MAX_REMINDER_WINDOW_KM = 9_000
    }
}
