package org.alberto97.aircontroller.provider.features.controllers

import org.alberto97.aircontroller.common.models.AppDeviceState

interface Controller<T> {
    fun getValue(state: AppDeviceState): T?
}