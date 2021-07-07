package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.SleepModeData
import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.ISupportedSleepModesController
import org.alberto97.hisenseair.models.AppDeviceState

class SupportedSleepModesController  : ControllerBase<List<SleepModeData>>(), ISupportedSleepModesController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): List<SleepModeData> {
        return state.supportedSleepModes
    }
}