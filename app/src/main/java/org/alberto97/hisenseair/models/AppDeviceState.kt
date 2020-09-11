package org.alberto97.hisenseair.models

import org.alberto97.hisenseair.features.FanSpeed
import org.alberto97.hisenseair.features.TempType
import org.alberto97.hisenseair.features.WorkMode


class AppDeviceState {
    var productName: String = "Device"
    var backlight: Boolean = false
    var supportedModes = emptyList<WorkMode>()
    var workMode: WorkMode = WorkMode.Auto
    var horizontalAirFlow: Boolean = false
    var verticalAirFlow: Boolean = false
    var quiet: Boolean = false
    var eco: Boolean = false
    var boost: Boolean = false
    var sleepMode: Int = 0
    var supportedSpeeds = emptyList<FanSpeed>()
    var fanSpeed: FanSpeed = FanSpeed.Auto
    var temp: Int = 0
    var roomTemp: Int = 0
    var tempUnit: TempType = TempType.Fahrenheit
    var on: Boolean = false
    var maxTemp = 90
    var minTemp = 61
}