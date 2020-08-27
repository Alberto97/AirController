package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.IQuietController
import org.alberto97.hisenseair.models.AppDeviceState

class QuietController : ControllerBase<Boolean>(), IQuietController {

    override fun isAvailable(state: AppDeviceState): Boolean {
        val disabled = state.workMode == WorkMode.Dry || state.workMode == WorkMode.Auto
        return !disabled
    }

    override fun getProperty(state: AppDeviceState): Boolean {
        return state.quiet
    }

}