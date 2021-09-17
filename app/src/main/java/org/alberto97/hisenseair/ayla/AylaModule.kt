package org.alberto97.hisenseair.ayla

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.alberto97.hisenseair.ayla.features.controllers.*
import org.alberto97.hisenseair.ayla.features.converters.*
import org.alberto97.hisenseair.ayla.network.api.AylaLogin
import org.alberto97.hisenseair.ayla.network.api.AylaService
import org.alberto97.hisenseair.ayla.network.api.PairApi
import org.alberto97.hisenseair.ayla.network.interceptors.AuthInterceptor
import org.alberto97.hisenseair.ayla.network.interceptors.TokenAuthenticator
import org.alberto97.hisenseair.ayla.repositories.*
import org.alberto97.hisenseair.features.controllers.*
import org.alberto97.hisenseair.repositories.IAuthenticationRepository
import org.alberto97.hisenseair.repositories.IDeviceControlRepository
import org.alberto97.hisenseair.repositories.IDevicePairRepository
import org.alberto97.hisenseair.repositories.IDeviceRepository
import org.alberto97.hisenseair.utils.IProviderModuleLoader
import org.alberto97.hisenseair.utils.Provider
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val aylaEuApi = module {
    single { getAylaApi(get(), "https://ads-eu.aylanetworks.com/apiv1/") }
    single { getAylaLogin("https://user-field-eu.aylanetworks.com/users/") }
}

val aylaUsApi = module {
    single { getAylaApi(get(), "https://ads-field.aylanetworks.com/apiv1/") }
    single { getAylaLogin("https://user-field.aylanetworks.com/users/") }
}

/**
 * Module internal dependencies
 */
val aylaInternal = module {

    // API
    single { getOkHttp(get()) }
    single { getPairApi(get()) }

    // Converter
    single<IBooleanConverter> { BooleanConverter() }
    single<IFanSpeedConverter> { FanSpeedConverter() }
    single<IIntConverter> { IntConverter() }
    single<IModeConverter> { ModeConverter() }
    single<ISleepModeConverter> { SleepModeConverter() }
    single<IStringConverter> { StringConverter() }
    single<ITempUnitConverter> { TempUnitConverter() }

    // Repositories
    single<IDevicePropertyRepository> { DevicePropertyRepository(get()) }
    single<IDeviceCacheRepository> { DeviceCacheRepository(get(), get()) }
    single<ISecretsRepository> { SecretsRepository(get()) }
}

val aylaControllers = module {
    single<IAirFlowHorizontalController> { AirFlowHorizontalController() }
    single<IAirFlowVerticalController> { AirFlowVerticalController() }
    single<IBacklightController> { BacklightController() }
    single<IBoostController> { BoostController() }
    single<IEcoController> { EcoController() }
    single<IFanSpeedController> { FanSpeedController() }
    single<IMaxTempController> { MaxTempController() }
    single<IMinTempController> { MinTempController() }
    single<IModeController> { ModeController() }
    single<IPowerController> { PowerController() }
    single<IQuietController> { QuietController() }
    single<IRoomTempController> { RoomTempController() }
    single<ISleepModeController> { SleepModeController() }
    single<ISupportedFanSpeedController> { SupportedFanSpeedController() }
    single<ISupportedModesController> { SupportedModesController() }
    single<ISupportedSleepModesController> { SupportedSleepModesController() }
    single<ITempControlController> { TempControlController() }
}

/**
 * Exposed APIs to the app
 */
val aylaModule = aylaInternal + aylaControllers + module {
    single<IAuthenticationRepository> { AuthenticationRepository(get(), get(), get()) }
    single<IDeviceRepository> { DeviceRepository(get(), get(), get(), get()) }
    single<IDeviceControlRepository> {
        DeviceControlRepository(get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    }
    single<IDevicePairRepository> { DevicePairRepository(get(), get()) }
}

val aylaLoaderModule = module {
    single<IProviderModuleLoader>(named(Provider.Ayla)){ AylaModuleLoader() }
}

fun getOkHttp(repo: IAuthenticationRepository): OkHttpClient {
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

fun getPairApi(httpClient: OkHttpClient) : PairApi {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.1/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(PairApi::class.java)
}

fun getAylaApi(httpClient: OkHttpClient, baseUrl: String) : AylaService {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(AylaService::class.java)
}

fun getAylaLogin(baseUrl: String): AylaLogin {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(AylaLogin::class.java)
}