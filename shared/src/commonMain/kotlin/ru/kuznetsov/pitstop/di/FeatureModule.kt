package ru.kuznetsov.pitstop.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.kuznetsov.pitstop.AppViewModel
import ru.kuznetsov.pitstop.features.addcar.AddCarViewModel
import ru.kuznetsov.pitstop.features.cardetail.CarDetailViewModel
import ru.kuznetsov.pitstop.features.cars.CarsViewModel
import ru.kuznetsov.pitstop.features.history.HistoryViewModel
import ru.kuznetsov.pitstop.features.settings.SettingsViewModel

val featureModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::CarsViewModel)
    viewModelOf(::AddCarViewModel)
    viewModelOf(::CarDetailViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::SettingsViewModel)
}
