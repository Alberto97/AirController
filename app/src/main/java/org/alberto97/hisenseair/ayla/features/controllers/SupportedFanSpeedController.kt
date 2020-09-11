package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.FanSpeed
import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.ISupportedFanSpeedController
import org.alberto97.hisenseair.models.AppDeviceState

class SupportedFanSpeedController : ControllerBase<List<FanSpeed>>(), ISupportedFanSpeedController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): List<FanSpeed> {
        return state.supportedSpeeds
    }
}