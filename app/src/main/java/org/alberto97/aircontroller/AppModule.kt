package org.alberto97.aircontroller

import org.alberto97.aircontroller.connectivity.IPairConnectivityManager
import org.alberto97.aircontroller.connectivity.PairConnectivityManager
import org.alberto97.aircontroller.common.repositories.ISettingsRepository
import org.alberto97.aircontroller.repositories.SettingsRepository
import org.alberto97.aircontroller.utils.DeviceShortcutManager
import org.alberto97.aircontroller.utils.IDeviceShortcutManager
import org.alberto97.aircontroller.utils.IProviderManager
import org.alberto97.aircontroller.utils.ProviderManager
import org.alberto97.aircontroller.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<ISettingsRepository> { SettingsRepository(get()) }
    single<IProviderManager> { ProviderManager(get()) }
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
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { SplashViewModel(get(), get()) }
    viewModel { PairViewModel(get(), get()) }
}