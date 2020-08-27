package org.alberto97.hisenseair.ayla.features.controllers

import org.alberto97.hisenseair.features.controllers.ControllerBase
import org.alberto97.hisenseair.features.controllers.IRoomTempController
import org.alberto97.hisenseair.models.AppDeviceState

class RoomTempController : ControllerBase<Int>(), IRoomTempController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): Int {
        return state.roomTemp
    }
}