package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.IAirFlowVerticalController
import org.alberto97.hisenseair.models.AppDeviceState

class AirFlowVerticalController : ControllerBase<Boolean>(), IAirFlowVerticalController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): Boolean {
        return state.verticalAirFlow
    }
}