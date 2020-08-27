package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.IMaxTempController
import org.alberto97.hisenseair.models.AppDeviceState

class MaxTempController : ControllerBase<Int>(), IMaxTempController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): Int {
        return state.maxTemp
    }
}