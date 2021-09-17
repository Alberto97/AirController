package org.alberto97.hisenseair.models

import org.alberto97.hisenseair.features.*


data class AppDeviceState(
    var productName: String = "Device",
    var backlight: Boolean = false,
    var supportedModes: List<WorkMode> = emptyList(),
    var workMode: WorkMode = WorkMode.Auto,
    var horizontalAirFlow: Boolean = false,
    var verticalAirFlow: Boolean = false,
    var quiet: Boolean = false,
    var eco: Boolean = false,
    var boost: Boolean = false,
    var sleepMode: SleepMode = SleepMode.OFF,
    var supportedSleepModes: List<SleepModeData> = emptyList(),
    var supportedSpeeds: List<FanSpeed> = emptyList(),
    var fanSpeed: FanSpeed = FanSpeed.Auto,
    var temp: Int = 0,
    var roomTemp: Int = 0,
    var tempUnit: TempType = TempType.Fahrenheit,
    var on: Boolean = false,
    var maxTemp: Int = 90,
    var minTemp: Int = 61
)