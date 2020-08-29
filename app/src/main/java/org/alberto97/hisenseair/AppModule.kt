package org.alberto97.hisenseair

import org.alberto97.hisenseair.viewmodels.DevicePreferenceViewModel
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.alberto97.hisenseair.viewmodels.LoginViewModel
import org.alberto97.hisenseair.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { DeviceViewModel(get()) }
    viewModel { DevicePreferenceViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
}