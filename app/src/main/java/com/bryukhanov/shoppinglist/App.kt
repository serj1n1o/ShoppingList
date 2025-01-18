package com.bryukhanov.shoppinglist

import android.app.Application
import com.bryukhanov.shoppinglist.di.dataModule
import com.bryukhanov.shoppinglist.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger(level = Level.DEBUG)
            }
            androidContext(this@App)
            modules(viewModelModule, dataModule)
        }
    }
}