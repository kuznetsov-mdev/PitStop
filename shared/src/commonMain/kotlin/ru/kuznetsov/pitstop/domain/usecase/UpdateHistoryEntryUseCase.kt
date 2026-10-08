package ru.kuznetsov.pitstop.domain.usecase

import ru.kuznetsov.pitstop.domain.model.ServiceHistoryEntry

/**
 * Edits a service history record, for example to fill in its cost and service name.
 * Not specified yet: the method is a placeholder.
 */
class UpdateHistoryEntryUseCase {
    suspend operator fun invoke(entry: ServiceHistoryEntry) {
    }
}
