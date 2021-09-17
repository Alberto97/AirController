package org.alberto97.hisenseair

import org.alberto97.hisenseair.connectivity.IPairConnectivityManager
import org.alberto97.hisenseair.connectivity.PairConnectivityManager
import org.alberto97.hisenseair.repositories.ISettingsRepository
import org.alberto97.hisenseair.repositories.SettingsRepository
import org.alberto97.hisenseair.utils.DeviceShortcutManager
import org.alberto97.hisenseair.utils.IDeviceShortcutManager
import org.alberto97.hisenseair.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<ISettingsRepository> { SettingsRepository(get()) }
    single<IPairConnectivityManager> { PairConnectivityManager(get()) }
    single<IDeviceShortcutManager> { DeviceShortcutManager(get()) }

    viewModel { parameters ->
        DeviceViewModel(
            parameters.get(), get(), get(), get(), get(), get(), get(), get(), get(), get(),
            get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()
        )
    }
    viewModel { parameters -> DevicePreferenceViewModel(parameters.get(), get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { SplashViewModel(get(), get()) }
    viewModel { PairViewModel(get(), get()) }
}