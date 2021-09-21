package org.alberto97.hisenseair.provider.ayla.features.controllers

import org.alberto97.hisenseair.common.models.AppDeviceState
import org.alberto97.hisenseair.provider.features.controllers.ControllerBase
import org.alberto97.hisenseair.provider.features.controllers.IPowerController


class PowerController : ControllerBase<Boolean>(), IPowerController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): Boolean {
        return state.on
    }
}