package org.alberto97.hisenseair

import android.app.Application
import org.alberto97.hisenseair.ayla.IAylaModuleLoader
import org.alberto97.hisenseair.ayla.aylaLoaderModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
class App : Application() {

    private val aylaModuleLoader: IAylaModuleLoader by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG)
                androidLogger(Level.ERROR)

            // Android context
            androidContext(this@App)

            // modules
            modules(appModule + aylaLoaderModule)
            aylaModuleLoader.load()
        }
    }
}