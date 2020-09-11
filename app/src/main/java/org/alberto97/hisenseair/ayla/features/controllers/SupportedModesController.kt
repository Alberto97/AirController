package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.ISupportedModesController
import org.alberto97.hisenseair.models.AppDeviceState

class SupportedModesController : ControllerBase<List<WorkMode>>(), ISupportedModesController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): List<WorkMode> {
        return state.supportedModes
    }
}