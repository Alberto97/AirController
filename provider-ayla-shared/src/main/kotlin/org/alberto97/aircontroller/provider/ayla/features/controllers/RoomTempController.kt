package org.alberto97.aircontroller.provider.ayla.features.controllers

import org.alberto97.aircontroller.common.models.AppDeviceState
import org.alberto97.aircontroller.provider.features.controllers.ControllerBase
import org.alberto97.aircontroller.provider.features.controllers.IRoomTempController


class RoomTempController : ControllerBase<Int>(), IRoomTempController {
    override fun isAvailable(state: AppDeviceState): Boolean {
        return true
    }

    override fun getProperty(state: AppDeviceState): Int {
        return state.roomTemp
    }
}