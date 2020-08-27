package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.ISleepModeController
import org.alberto97.hisenseair.models.AppDeviceState

class SleepModeController : ControllerBase<Int>(), ISleepModeController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): Int {
        return state.sleepMode
    }

}