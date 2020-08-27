package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.IBacklightController
import org.alberto97.hisenseair.models.AppDeviceState

class BacklightController : ControllerBase<Boolean>(), IBacklightController {

    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): Boolean {
        return state.backlight
    }
}