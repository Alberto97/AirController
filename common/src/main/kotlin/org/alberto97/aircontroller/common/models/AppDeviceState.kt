package org.alberto97.aircontroller.common.models

import org.alberto97.aircontroller.common.features.*


data class AppDeviceState(
    val productName: String,
    val backlight: Boolean,
    val supportedModes: List<WorkMode>,
    val workMode: WorkMode,
    val horizontalAirFlow: Boolean,
    val verticalAirFlow: Boolean,
    val quiet: Boolean,
    val eco: Boolean,
    val boost: Boolean,
    val sleepMode: SleepMode,
    val supportedSleepModes: List<SleepModeData>,
    val supportedSpeeds: List<FanSpeed>,
    val fanSpeed: FanSpeed,
    val temp: Int,
    val roomTemp: Int,
    val tempUnit: TempType,
    val on: Boolean,
    val maxTemp: Int,
    val minTemp: Int
)