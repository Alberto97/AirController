package org.alberto97.aircontroller.provider.ayla.features.controllers

import org.alberto97.aircontroller.common.features.FanSpeed
import org.alberto97.aircontroller.common.models.AppDeviceState
import org.alberto97.aircontroller.provider.features.controllers.ControllerBase
import org.alberto97.aircontroller.provider.features.controllers.ISupportedFanSpeedController


class SupportedFanSpeedController : ControllerBase<List<FanSpeed>>(), ISupportedFanSpeedController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): List<FanSpeed> {
        return state.supportedSpeeds
    }
}