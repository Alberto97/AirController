package org.alberto97.aircontroller.provider.ayla

import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.alberto97.aircontroller.common.enums.Provider
import org.alberto97.aircontroller.provider.ayla.internal.converters.*
import org.alberto97.aircontroller.provider.ayla.internal.network.api.AylaLogin
import org.alberto97.aircontroller.provider.ayla.internal.network.api.AylaService
import org.alberto97.aircontroller.provider.ayla.internal.network.api.PairApi
import org.alberto97.aircontroller.provider.ayla.internal.network.interceptors.AuthInterceptor
import org.alberto97.aircontroller.provider.ayla.internal.network.interceptors.TokenAuthenticator
import org.alberto97.aircontroller.provider.ayla.internal.repositories.*
import org.alberto97.aircontroller.provider.ayla.repositories.AuthenticationRepository
import org.alberto97.aircontroller.provider.ayla.repositories.DeviceControlRepository
import org.alberto97.aircontroller.provider.ayla.repositories.DevicePairRepository
import org.alberto97.aircontroller.provider.ayla.repositories.DeviceRepository
import org.alberto97.aircontroller.provider.ayla.serialization.MoshiAylaExtensions.addAyla
import org.alberto97.aircontroller.provider.repositories.IAuthenticationRepository
import org.alberto97.aircontroller.provider.repositories.IDeviceControlRepository
import org.alberto97.aircontroller.provider.repositories.IDevicePairRepository
import org.alberto97.aircontroller.provider.repositories.IDeviceRepository
import org.alberto97.aircontroller.provider.utils.IProviderModuleLoader
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal val aylaEuApi = module {
    single { getAylaApi(get(), get(), "https://ads-eu.aylanetworks.com/apiv1/") }
    single { getAylaLogin(get(), "https://user-field-eu.aylanetworks.com/users/") }
}

internal val aylaUsApi = module {
    single { getAylaApi(get(), get(),"https://ads-field.aylanetworks.com/apiv1/") }
    single { getAylaLogin(get(), "https://user-field.aylanetworks.com/users/") }
}

/**
 * Module internal dependencies
 */
private val aylaInternal = module {
    single { getMoshi() }

    // API
    single { getOkHttp(get()) }
    single { getPairApi(get(), get()) }

    // Converter
    single<IBooleanConverter> { BooleanConverter() }
    single<IFanSpeedConverter> { FanSpeedConverter() }
    single<IIntConverter> { IntConverter() }
    single<IModeConverter> { ModeConverter() }
    single<ISleepModeConverter> { SleepModeConverter() }
    single<ITempUnitConverter> { TempUnitConverter() }

    // Repositories
    single<IDevicePropertyRepository> { DevicePropertyRepository(get()) }
    single<IDeviceCacheRepository> { DeviceCacheRepository(get(), get()) }
    single<ISecretsRepository> { SecretsRepository(get()) }
}

/**
 * Exposed APIs to the app
 */
val aylaModule = aylaInternal + aylaControllers + module {
    single<IAuthenticationRepository> { AuthenticationRepository(get(), get(), get()) }
    single<IDeviceRepository> { DeviceRepository(get(), get(), get(), get()) }
    single<IDeviceControlRepository> {
        DeviceControlRepository(get(), get(), get(), get(), get(), get(), get(), get())
    }
    single<IDevicePairRepository> { DevicePairRepository(get(), get()) }
}

val aylaLoaderModule = module {
    single<IProviderModuleLoader>(named(Provider.Ayla)) { AylaModuleLoader() }
}

private fun getOkHttp(repo: IAuthenticationRepository): OkHttpClient {
    return OkHttpClient.Builder()
        .authenticator(TokenAuthenticator(repo))
        .addInterceptor(AuthInterceptor(repo))
        .addNetworkInterceptor { chain: Interceptor.Chain ->
            val request = chain.request()
                .newBuilder()
                .removeHeader("Accept-Encoding")
                .removeHeader("User-Agent")
                .build()
            chain.proceed(request)
        }
        .build()
}

private fun getMoshi(): Moshi {
    return Moshi.Builder()
        .addAyla()
        .build()
}

private fun getPairApi(httpClient: OkHttpClient, moshi: Moshi): PairApi {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.1/")
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    return retrofit.create(PairApi::class.java)
}

private fun getAylaApi(httpClient: OkHttpClient, moshi: Moshi, baseUrl: String): AylaService {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    return retrofit.create(AylaService::class.java)
}

private fun getAylaLogin(moshi: Moshi, baseUrl: String): AylaLogin {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    return retrofit.create(AylaLogin::class.java)
}