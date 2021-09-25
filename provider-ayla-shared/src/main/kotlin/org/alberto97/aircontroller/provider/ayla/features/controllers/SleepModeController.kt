package org.alberto97.aircontroller.provider.ayla.features.controllers

import org.alberto97.aircontroller.common.features.SleepMode
import org.alberto97.aircontroller.common.features.WorkMode
import org.alberto97.aircontroller.common.models.AppDeviceState
import org.alberto97.aircontroller.provider.features.controllers.ControllerBase
import org.alberto97.aircontroller.provider.features.controllers.ISleepModeController


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