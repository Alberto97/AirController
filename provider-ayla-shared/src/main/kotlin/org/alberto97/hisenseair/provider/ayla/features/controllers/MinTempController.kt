package org.alberto97.hisenseair.provider.ayla.features.controllers

import org.alberto97.hisenseair.common.models.AppDeviceState
import org.alberto97.hisenseair.provider.features.controllers.ControllerBase
import org.alberto97.hisenseair.provider.features.controllers.IMinTempController


class MinTempController : ControllerBase<Int>(), IMinTempController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): Int {
        return state.minTemp
    }
}