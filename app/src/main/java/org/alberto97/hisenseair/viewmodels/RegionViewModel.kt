package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.ViewModel
import org.alberto97.hisenseair.ayla.IAylaModuleLoader
import org.alberto97.hisenseair.repositories.ISettingsRepository
import org.alberto97.hisenseair.repositories.Region

class RegionViewModel(private val settings: ISettingsRepository, private val moduleLoader: IAylaModuleLoader) : ViewModel() {

    fun setRegion(region: Region) {
        settings.region = region
        moduleLoader.load(region)
    }
}