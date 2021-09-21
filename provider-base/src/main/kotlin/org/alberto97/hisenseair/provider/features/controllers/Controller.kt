package org.alberto97.hisenseair.provider.features.controllers

import org.alberto97.hisenseair.common.models.AppDeviceState

interface Controller<T> {
    fun getValue(state: AppDeviceState): T?
}