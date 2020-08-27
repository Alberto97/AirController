package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.IAirFlowHorizontalController
import org.alberto97.hisenseair.models.AppDeviceState

class AirFlowHorizontalController : ControllerBase<Boolean>(), IAirFlowHorizontalController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): Boolean {
        return state.horizontalAirFlow
    }
}