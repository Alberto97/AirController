package org.alberto97.hisenseair

import android.app.Application
import org.alberto97.hisenseair.provider.ayla.aylaLoaderModule
import org.alberto97.hisenseair.provider.demo.demoLoaderModule
import org.alberto97.hisenseair.utils.IProviderManager
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
class App : Application() {

    private val providerManager: IProviderManager by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG)
                androidLogger(Level.ERROR)

            // Android context
            androidContext(this@App)

            // modules
            modules(appModule + aylaLoaderModule + demoLoaderModule)
            providerManager.loadModule()
        }
    }
}