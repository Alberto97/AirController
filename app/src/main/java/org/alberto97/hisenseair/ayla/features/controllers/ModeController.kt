package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.IModeController
import org.alberto97.hisenseair.models.AppDeviceState

class ModeController : ControllerBase<WorkMode>(), IModeController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): WorkMode {
        return state.workMode
    }
}