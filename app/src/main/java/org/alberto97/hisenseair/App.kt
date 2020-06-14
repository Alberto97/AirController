package org.alberto97.hisenseair

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import okhttp3.OkHttpClient
import org.alberto97.hisenseair.interceptors.AuthInterceptor
import org.alberto97.hisenseair.interceptors.TokenAuthenticator
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG)
                androidLogger()

            // Android context
            androidContext(this@App)

            // modules
            modules(appModule)
        }
    }
}