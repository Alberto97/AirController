package org.alberto97.hisenseair.provider.ayla.features.controllers

import org.alberto97.hisenseair.common.features.WorkMode
import org.alberto97.hisenseair.common.models.AppDeviceState
import org.alberto97.hisenseair.provider.features.controllers.ControllerBase
import org.alberto97.hisenseair.provider.features.controllers.IModeController


class ModeController : ControllerBase<WorkMode>(), IModeController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): WorkMode {
        return state.workMode
    }
}