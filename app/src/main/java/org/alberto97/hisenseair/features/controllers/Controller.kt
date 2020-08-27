package org.alberto97.hisenseair.features.controllers

import org.alberto97.hisenseair.models.AppDeviceState

interface Controller<T> {
    fun getValue(state: AppDeviceState): T?
}