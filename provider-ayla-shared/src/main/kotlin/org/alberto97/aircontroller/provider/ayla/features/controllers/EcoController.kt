package org.alberto97.aircontroller.provider.ayla.features.controllers

import org.alberto97.aircontroller.common.models.AppDeviceState
import org.alberto97.aircontroller.provider.features.controllers.ControllerBase
import org.alberto97.aircontroller.provider.features.controllers.IEcoController


class EcoController : ControllerBase<Boolean>(), IEcoController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): Boolean {
        return state.eco
    }
}