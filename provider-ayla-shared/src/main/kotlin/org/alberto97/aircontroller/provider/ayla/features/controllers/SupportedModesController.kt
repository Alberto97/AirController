package org.alberto97.aircontroller.provider.ayla.features.controllers

import org.alberto97.aircontroller.common.features.WorkMode
import org.alberto97.aircontroller.common.models.AppDeviceState
import org.alberto97.aircontroller.provider.features.controllers.ControllerBase
import org.alberto97.aircontroller.provider.features.controllers.ISupportedModesController


class SupportedModesController : ControllerBase<List<WorkMode>>(), ISupportedModesController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): List<WorkMode> {
        return state.supportedModes
    }
}