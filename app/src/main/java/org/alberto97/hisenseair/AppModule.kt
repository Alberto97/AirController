package org.alberto97.hisenseair

import org.alberto97.hisenseair.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { parameters ->
        DeviceViewModel(
            parameters.get(), get(), get(), get(), get(), get(), get(), get(), get(), get(),
            get(), get(), get(), get(), get(), get(), get(), get(), get()
        )
    }
    viewModel {  parameters -> DevicePreferenceViewModel(parameters.get(), get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { SplashViewModel(get()) }
}