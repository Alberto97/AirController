package org.alberto97.hisenseair.provider.ayla.features.controllers

import org.alberto97.hisenseair.common.models.AppDeviceState
import org.alberto97.hisenseair.provider.features.controllers.ControllerBase
import org.alberto97.hisenseair.provider.features.controllers.IMaxTempController


class MaxTempController : ControllerBase<Int>(), IMaxTempController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): Int {
        return state.maxTemp
    }
}