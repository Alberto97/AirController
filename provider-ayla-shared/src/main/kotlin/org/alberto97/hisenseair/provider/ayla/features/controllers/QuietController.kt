package org.alberto97.hisenseair.provider.ayla.features.controllers

import org.alberto97.hisenseair.common.features.WorkMode
import org.alberto97.hisenseair.common.models.AppDeviceState
import org.alberto97.hisenseair.provider.features.controllers.ControllerBase
import org.alberto97.hisenseair.provider.features.controllers.IQuietController

class QuietController : ControllerBase<Boolean>(), IQuietController {

    override fun isAvailable(state: AppDeviceState): Boolean {
        val disabled = state.workMode == WorkMode.Dry || state.workMode == WorkMode.Auto
        return !disabled
    }

    override fun getProperty(state: AppDeviceState): Boolean {
        return state.quiet
    }

}