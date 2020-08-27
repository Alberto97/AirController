package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.IBoostController
import org.alberto97.hisenseair.models.AppDeviceState

class BoostController : ControllerBase<Boolean>(), IBoostController {

    override fun isAvailable(state: AppDeviceState): Boolean {
        return state.workMode == WorkMode.Cooling || state.workMode == WorkMode.Heating
    }

    override fun getProperty(state: AppDeviceState): Boolean {
        return state.boost
    }

}