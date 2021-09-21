package org.alberto97.hisenseair.provider.ayla

import android.util.Log
import org.alberto97.hisenseair.common.enums.Region
import org.alberto97.hisenseair.provider.utils.IProviderModuleLoader
import org.koin.core.context.loadKoinModules


class AylaModuleLoader : IProviderModuleLoader {

    private val map = mapOf(
        Region.EU to aylaEuApi,
        Region.US to aylaUsApi
    )

    override fun load() {
        loadKoinModules(aylaModule)
    }

    override fun setRegion(region: Region) {
        val module = map[region]
        if (module != null) {
            loadKoinModules(module)
        } else {
            Log.e("AylaEnvironmentManager", "Cannot load region tied ayla modules, unknown country")
        }
    }
}