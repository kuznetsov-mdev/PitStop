package ru.kuznetsov.pitstop.domain.usecase

import kotlinx.datetime.LocalDate
import ru.kuznetsov.pitstop.domain.model.TaskReminder

/**
 * Picks the tasks to send push notifications about.
 * Not specified yet: the method is a placeholder and returns nothing.
 */
class GetTaskRemindersUseCase {
    suspend operator fun invoke(today: LocalDate): List<TaskReminder> = emptyList()
}
