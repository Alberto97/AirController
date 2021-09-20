package org.alberto97.hisenseair.models

class DevicePairStatus(
    val id: String,
    val name: String
)

enum class WifiSecurity {
    OPEN,
    PROTECTED
}

class DevicePairWifiScanResult(
    val ssid: String,
    val security: WifiSecurity,
    val signalStrengthBars: Int
)