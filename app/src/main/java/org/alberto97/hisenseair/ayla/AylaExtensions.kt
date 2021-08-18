package org.alberto97.hisenseair.ayla

import org.alberto97.hisenseair.ayla.models.Device
import org.alberto97.hisenseair.ayla.models.WifiScanResults

object AylaExtensions {

    fun Device.isAvailable(): Boolean {
        return this.connectionStatus == AylaDeviceState.ONLINE
    }

    fun WifiScanResults.WifiScanResult.isOpen(): Boolean {
        return security == "None"
    }
}