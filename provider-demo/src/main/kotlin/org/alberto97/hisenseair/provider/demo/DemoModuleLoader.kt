package org.alberto97.hisenseair.provider.demo

import org.alberto97.hisenseair.common.enums.Region
import org.alberto97.hisenseair.provider.utils.IProviderModuleLoader
import org.koin.core.context.loadKoinModules


class DemoModuleLoader : IProviderModuleLoader {

    override fun load() {
        loadKoinModules(demoModule)
    }

    override fun setRegion(region: Region) {
        // No-op
    }
}