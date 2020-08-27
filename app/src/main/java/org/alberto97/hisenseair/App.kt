package org.alberto97.hisenseair

import android.app.Application
import org.alberto97.hisenseair.ayla.aylaModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG)
                androidLogger()

            // Android context
            androidContext(this@App)

            // modules
            modules(appModule + aylaModule)
        }
    }
}