package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.ITempControlController
import org.alberto97.hisenseair.models.AppDeviceState

class TempControlController : ControllerBase<Int>(), ITempControlController {

    override fun isAvailable(state: AppDeviceState): Boolean {
        return state.workMode == WorkMode.Cooling || state.workMode == WorkMode.Heating
    }

    override fun getProperty(state: AppDeviceState): Int {
        return state.temp
    }

}