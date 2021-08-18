package org.alberto97.hisenseair

import android.os.Build
import org.alberto97.hisenseair.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { parameters ->
        DeviceViewModel(
            parameters.get(), get(), get(), get(), get(), get(), get(), get(), get(), get(),
            get(), get(), get(), get(), get(), get(), get(), get(), get(), get()
        )
    }
    viewModel {  parameters -> DevicePreferenceViewModel(parameters.get(), get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { SplashViewModel(get()) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        viewModel { PairViewModel(get(), get()) }
}