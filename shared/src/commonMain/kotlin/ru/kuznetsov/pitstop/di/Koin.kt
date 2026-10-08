package ru.kuznetsov.pitstop.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(featureModule, domainModule, dataModule)
    }
}
