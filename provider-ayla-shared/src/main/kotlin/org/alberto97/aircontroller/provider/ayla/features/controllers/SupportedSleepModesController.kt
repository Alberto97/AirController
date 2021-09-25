package org.alberto97.aircontroller.provider.ayla.features.controllers

import org.alberto97.aircontroller.common.features.SleepModeData
import org.alberto97.aircontroller.common.models.AppDeviceState
import org.alberto97.aircontroller.provider.features.controllers.ControllerBase
import org.alberto97.aircontroller.provider.features.controllers.ISupportedSleepModesController


class SupportedSleepModesController  : ControllerBase<List<SleepModeData>>(),
    ISupportedSleepModesController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): List<SleepModeData> {
        return state.supportedSleepModes
    }
}