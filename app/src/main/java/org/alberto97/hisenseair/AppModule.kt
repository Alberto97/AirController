package org.alberto97.hisenseair

import android.os.Build
import org.alberto97.hisenseair.ayla.AylaModuleLoader
import org.alberto97.hisenseair.ayla.IAylaModuleLoader
import org.alberto97.hisenseair.repositories.ISettingsRepository
import org.alberto97.hisenseair.repositories.SettingsRepository
import org.alberto97.hisenseair.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<IAylaModuleLoader>{ AylaModuleLoader(get()) }
    single<ISettingsRepository> { SettingsRepository(get()) }

    viewModel { parameters ->
        DeviceViewModel(
            parameters.get(), get(), get(), get(), get(), get(), get(), get(), get(), get(),
            get(), get(), get(), get(), get(), get(), get(), get(), get(), get()
        )
    }
    viewModel { parameters -> DevicePreferenceViewModel(parameters.get(), get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { RegionViewModel(get()) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        viewModel { PairViewModel(get(), get()) }
}