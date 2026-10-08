package ru.kuznetsov.pitstop.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.kuznetsov.pitstop.domain.usecase.CalculateTaskStatusUseCase
import ru.kuznetsov.pitstop.domain.usecase.CarAlertsUseCase
import ru.kuznetsov.pitstop.domain.usecase.SortedTaskStatsUseCase

val domainModule = module {
    factoryOf(::CalculateTaskStatusUseCase)
    factoryOf(::SortedTaskStatsUseCase)
    factoryOf(::CarAlertsUseCase)
}
