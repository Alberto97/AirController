package org.alberto97.hisenseair.ayla

import org.alberto97.hisenseair.ayla.models.Device

object AylaExtensions {

    fun Device.isAvailable(): Boolean {
        return this.connectionStatus == AylaDeviceState.ONLINE
    }
}