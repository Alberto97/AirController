package org.alberto97.aircontroller.provider.demo

import org.alberto97.aircontroller.provider.ayla.aylaControllers
import org.alberto97.aircontroller.provider.demo.internal.DemoStateHolder
import org.alberto97.aircontroller.provider.demo.repositories.AuthenticationRepository
import org.alberto97.aircontroller.provider.demo.repositories.DeviceControlRepository
import org.alberto97.aircontroller.provider.demo.repositories.DevicePairRepository
import org.alberto97.aircontroller.provider.demo.repositories.DeviceRepository
import org.alberto97.aircontroller.provider.repositories.IAuthenticationRepository
import org.alberto97.aircontroller.provider.repositories.IDeviceControlRepository
import org.alberto97.aircontroller.provider.repositories.IDevicePairRepository
import org.alberto97.aircontroller.provider.repositories.IDeviceRepository
import org.alberto97.aircontroller.provider.utils.IProviderModuleLoader
import org.alberto97.aircontroller.common.enums.Provider
import org.koin.core.qualifier.named
import org.koin.dsl.module

val demoModule = aylaControllers + module {
    single { DemoStateHolder() }

    single<IAuthenticationRepository> { AuthenticationRepository(get()) }
    single<IDeviceRepository> { DeviceRepository(get()) }
    single<IDeviceControlRepository> { DeviceControlRepository(get()) }
    single<IDevicePairRepository> { DevicePairRepository() }
}

val demoLoaderModule = module {
    single<IProviderModuleLoader>(named(Provider.Demo)){ DemoModuleLoader() }
}