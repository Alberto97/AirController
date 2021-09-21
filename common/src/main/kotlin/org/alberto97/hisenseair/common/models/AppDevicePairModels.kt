package org.alberto97.hisenseair.common.models

import org.alberto97.hisenseair.common.enums.WifiSecurity

class DevicePairStatus(
    val id: String,
    val name: String
)

class DevicePairWifiScanResult(
    val ssid: String,
    val security: WifiSecurity,
    val signalStrengthBars: Int
)