package org.alberto97.hisenseair.provider.utils

import org.alberto97.hisenseair.common.enums.Region

interface IProviderModuleLoader {
    fun load()
    fun setRegion(region: Region)
}