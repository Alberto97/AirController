package org.alberto97.hisenseair.provider.ayla.features.controllers

import org.alberto97.hisenseair.common.features.WorkMode
import org.alberto97.hisenseair.common.models.AppDeviceState
import org.alberto97.hisenseair.provider.features.controllers.ControllerBase
import org.alberto97.hisenseair.provider.features.controllers.ITempControlController


class TempControlController : ControllerBase<Int>(), ITempControlController {

    override fun isAvailable(state: AppDeviceState): Boolean {
        return state.workMode == WorkMode.Cooling || state.workMode == WorkMode.Heating
    }

    override fun getProperty(state: AppDeviceState): Int {
        return state.temp
    }

}