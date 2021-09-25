package org.alberto97.aircontroller.provider.utils

import org.alberto97.aircontroller.common.enums.Region

interface IProviderModuleLoader {
    fun load()
    fun setRegion(region: Region)
}