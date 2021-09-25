package org.alberto97.aircontroller.provider.ayla.features.controllers

import org.alberto97.aircontroller.common.features.FanSpeed
import org.alberto97.aircontroller.common.models.AppDeviceState
import org.alberto97.aircontroller.provider.features.controllers.ControllerBase
import org.alberto97.aircontroller.provider.features.controllers.IFanSpeedController


class FanSpeedController : ControllerBase<FanSpeed>(), IFanSpeedController {

    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): FanSpeed {
        return state.fanSpeed
    }

}