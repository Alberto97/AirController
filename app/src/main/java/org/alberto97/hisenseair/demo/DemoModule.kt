package org.alberto97.hisenseair.demo

import org.alberto97.hisenseair.ayla.aylaControllers
import org.alberto97.hisenseair.demo.internal.DemoStateHolder
import org.alberto97.hisenseair.demo.repositories.AuthenticationRepository
import org.alberto97.hisenseair.demo.repositories.DeviceControlRepository
import org.alberto97.hisenseair.demo.repositories.DevicePairRepository
import org.alberto97.hisenseair.demo.repositories.DeviceRepository
import org.alberto97.hisenseair.repositories.IAuthenticationRepository
import org.alberto97.hisenseair.repositories.IDeviceControlRepository
import org.alberto97.hisenseair.repositories.IDevicePairRepository
import org.alberto97.hisenseair.repositories.IDeviceRepository
import org.alberto97.hisenseair.utils.IProviderModuleLoader
import org.alberto97.hisenseair.utils.Provider
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