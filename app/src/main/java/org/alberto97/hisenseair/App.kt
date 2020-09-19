package org.alberto97.hisenseair

import android.app.Application
import org.alberto97.hisenseair.ayla.aylaModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG)
                androidLogger(Level.ERROR)

            // Android context
            androidContext(this@App)

            // modules
            modules(appModule + aylaModule)
        }
    }
}