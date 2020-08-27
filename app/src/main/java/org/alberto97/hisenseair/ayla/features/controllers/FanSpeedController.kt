package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.FanSpeed
import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.IFanSpeedController
import org.alberto97.hisenseair.models.AppDeviceState

class FanSpeedController : ControllerBase<FanSpeed>(), IFanSpeedController {

    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): FanSpeed {
        return state.fanSpeed
    }

}