package ru.kuznetsov.pitstop.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kuznetsov.pitstop.data.repository.FakeCarRepositoryImpl
import ru.kuznetsov.pitstop.data.repository.FakeServiceHistoryRepositoryImpl
import ru.kuznetsov.pitstop.data.repository.FakeSettingsRepositoryImpl
import ru.kuznetsov.pitstop.domain.repository.CarRepository
import ru.kuznetsov.pitstop.domain.repository.ServiceHistoryRepository
import ru.kuznetsov.pitstop.domain.repository.SettingsRepository

val dataModule = module {
    singleOf(::FakeCarRepositoryImpl) bind CarRepository::class
    singleOf(::FakeServiceHistoryRepositoryImpl) bind ServiceHistoryRepository::class
    singleOf(::FakeSettingsRepositoryImpl) bind SettingsRepository::class
}
