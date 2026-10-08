package ru.kuznetsov.pitstop

import android.app.Application
import ru.kuznetsov.pitstop.di.initKoin

class PitStopApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
