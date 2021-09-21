package org.alberto97.hisenseair.provider.ayla.features.controllers

import org.alberto97.hisenseair.common.features.FanSpeed
import org.alberto97.hisenseair.common.models.AppDeviceState
import org.alberto97.hisenseair.provider.features.controllers.ControllerBase
import org.alberto97.hisenseair.provider.features.controllers.ISupportedFanSpeedController


class SupportedFanSpeedController : ControllerBase<List<FanSpeed>>(), ISupportedFanSpeedController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): List<FanSpeed> {
        return state.supportedSpeeds
    }
}