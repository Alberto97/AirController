package org.alberto97.hisenseair

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.alberto97.hisenseair.interceptors.AuthInterceptor
import org.alberto97.hisenseair.interceptors.TokenAuthenticator
import org.alberto97.hisenseair.repositories.AuthenticationRepository
import org.alberto97.hisenseair.repositories.DeviceRepository
import org.alberto97.hisenseair.repositories.IAuthenticationRepository
import org.alberto97.hisenseair.repositories.IDeviceRepository
import org.alberto97.hisenseair.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { getOkHttp(get()) }
    single { getAylaApi(get()) }
    single { getAylaLogin() }
    viewModel { DeviceViewModel(get()) }
    viewModel { DevicePreferenceViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
    single<IAuthenticationRepository> { AuthenticationRepository(get(), get()) }
    single<IDeviceRepository> { DeviceRepository(get()) }
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

fun getAylaApi(httpClient: OkHttpClient) : AylaService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://ads-eu.aylanetworks.com/apiv1/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(AylaService::class.java)
}

fun getAylaLogin(): AylaLogin {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://user-field-eu.aylanetworks.com/users/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(AylaLogin::class.java)
}