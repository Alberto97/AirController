package org.alberto97.aircontroller.provider.features.controllers

import org.alberto97.aircontroller.common.models.AppDeviceState

abstract class ControllerBase<T> : Controller<T> {
    abstract fun isAvailable(state: AppDeviceState): Boolean
    abstract fun getProperty(state: AppDeviceState): T

    override fun getValue(state: AppDeviceState): T? {
        return if (isAvailable(state))
            getProperty(state)
        else
            null
    }
}