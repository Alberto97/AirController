package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.ViewModel
import org.alberto97.hisenseair.repositories.ISettingsRepository
import org.alberto97.hisenseair.repositories.Region

class RegionViewModel(private val settings: ISettingsRepository) : ViewModel() {

    fun setRegion(region: Region) {
        settings.region = region
    }
}