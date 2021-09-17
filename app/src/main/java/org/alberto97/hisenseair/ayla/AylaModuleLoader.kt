package org.alberto97.hisenseair.ayla

import android.util.Log
import org.alberto97.hisenseair.repositories.ISettingsRepository
import org.alberto97.hisenseair.repositories.Region
import org.koin.core.context.loadKoinModules

interface IAylaModuleLoader {
    fun load()
    fun load(region: Region)
}

class AylaModuleLoader(private val settings: ISettingsRepository) : IAylaModuleLoader {

    private val map = mapOf(
        Region.EU to aylaEuApi,
        Region.US to aylaUsApi
    )

    override fun load() {
        val region = settings.region ?: return
        load(region)
    }

    override fun load(region: Region) {
        val module = map[region]
        if (module != null) {
            loadKoinModules(module + aylaModule)
        } else {
            Log.e("AylaEnvironmentManager", "Cannot load ayla module, unknown country")
        }
    }
}