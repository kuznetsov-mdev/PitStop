package ru.kuznetsov.pitstop.di

import org.koin.core.context.startKoin

/** Starts Koin with every app module; called once per process from the platform entry point, before the first composition. */
fun initKoin() {
    startKoin {
        modules(featureModule, domainModule, dataModule)
    }
}
