package org.alberto97.hisenseair.provider.ayla.features.controllers

import org.alberto97.hisenseair.common.features.SleepMode
import org.alberto97.hisenseair.common.features.WorkMode
import org.alberto97.hisenseair.common.models.AppDeviceState
import org.alberto97.hisenseair.provider.features.controllers.ControllerBase
import org.alberto97.hisenseair.provider.features.controllers.ISleepModeController


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