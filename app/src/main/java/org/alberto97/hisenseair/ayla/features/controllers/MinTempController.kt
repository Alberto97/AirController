package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.IMinTempController
import org.alberto97.hisenseair.models.AppDeviceState

class MinTempController : ControllerBase<Int>(), IMinTempController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): Int {
        return state.minTemp
    }
}