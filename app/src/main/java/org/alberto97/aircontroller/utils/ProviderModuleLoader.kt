package org.alberto97.aircontroller.utils

import org.alberto97.aircontroller.common.enums.Provider
import org.alberto97.aircontroller.common.repositories.ISettingsRepository
import org.alberto97.aircontroller.common.enums.Region
import org.alberto97.aircontroller.provider.utils.IProviderModuleLoader
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named

interface IProviderManager {
    fun loadModule()
    fun setProvider(provider: Provider)
    fun setRegion(region: Region)
}

class ProviderManager(private val settings: ISettingsRepository) : IProviderManager, KoinComponent {

    var provider: IProviderModuleLoader? = null

    private fun loadProvider(provider: Provider) {
        this.provider = get(named(provider))
        this.provider?.load()
    }

    override fun loadModule() {
        val provider = settings.provider
        val region = settings.region

        if (provider == null) return
        loadProvider(provider)

        if (region == null) return
        setRegion(region)
    }

    override fun setProvider(provider: Provider) {
        loadProvider(provider)
        settings.provider = provider
    }

    override fun setRegion(region: Region) {
        provider?.setRegion(region)
    }
}