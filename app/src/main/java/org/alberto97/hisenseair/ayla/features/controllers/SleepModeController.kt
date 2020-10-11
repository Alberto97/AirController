package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.SleepMode
import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.ISleepModeController
import org.alberto97.hisenseair.models.AppDeviceState

class SleepModeController : ControllerBase<SleepMode>(), ISleepModeController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return when(state.workMode) {
            WorkMode.Dry,
            WorkMode.Cooling,
            WorkMode.Heating -> true
            else -> false
        }
    }

    override fun getProperty(state: AppDeviceState): SleepMode {
        return state.sleepMode
    }

}