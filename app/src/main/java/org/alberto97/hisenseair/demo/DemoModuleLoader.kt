package org.alberto97.hisenseair.demo

import org.alberto97.hisenseair.repositories.Region
import org.alberto97.hisenseair.utils.IProviderModuleLoader
import org.koin.core.context.loadKoinModules


class DemoModuleLoader : IProviderModuleLoader {

    override fun load() {
        loadKoinModules(demoModule)
    }

    override fun setRegion(region: Region) {
        // No-op
    }
}