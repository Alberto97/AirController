package org.alberto97.aircontroller.provider.demo.internal

import org.alberto97.aircontroller.common.features.*
import org.alberto97.aircontroller.common.models.AppDevice
import org.alberto97.aircontroller.common.models.AppDeviceState

internal class DemoStateHolder {
    var devices: MutableMap<String, AppDevice>
    var states: MutableMap<String, AppDeviceState>

    init {
        devices = loadInitialDevices()
        states = loadInitialStates()
    }

    private fun loadInitialDevices(): MutableMap<String, AppDevice> {
        val device1 = AppDevice("A001", "Bedroom AC", true, "192.168.0.81", "FF:FF:FF:FF:FF:FF", "HomeWifi")
        val device2 = AppDevice("A002", "Living Room AC", true, "192.168.0.82", "FF:FF:FF:FF:FF:FF", "HomeWifi")

        return mutableMapOf(
            device1.id to device1,
            device2.id to device2
        )
    }

    private fun getDeviceState(): AppDeviceState {
        return AppDeviceState(
            productName = "Device",
            backlightType = BacklightType.Stateful,
            backlight = false,
            supportedModes = emptyList(),
            workMode = WorkMode.Auto,
            horizontalAirFlow = false,
            verticalAirFlow = false,
            quiet = false,
            eco = false,
            boost = false,
            sleepMode = SleepMode.OFF,
            supportedSleepModes = emptyList(),
            supportedSpeeds = emptyList(),
            fanSpeed = FanSpeed.Auto,
            temp = 0,
            roomTemp = 0,
            tempUnit = TempType.Fahrenheit,
            on = false,
            maxTemp = 90,
            minTemp = 61
        )
    }

    private fun loadInitialStates(): MutableMap<String, AppDeviceState> {
        val sleep1 = SleepModeData(SleepMode.MODE1, listOf(2), listOf(-2))
        val sleep2 = SleepModeData(SleepMode.MODE2, listOf(2, 6, 7), listOf(-2, -1, -1))
        val supportedSleepMode = listOf(sleep1, sleep2)
        val supportedModes = listOf(WorkMode.Heating, WorkMode.Cooling, WorkMode.Dry, WorkMode.FanOnly, WorkMode.Auto)
        val supportedFanSpeed = listOf(FanSpeed.Lower, FanSpeed.Low, FanSpeed.Normal, FanSpeed.High, FanSpeed.Higher, FanSpeed.Auto)

        val state1 = getDeviceState().copy(
            supportedModes = supportedModes,
            supportedSpeeds = supportedFanSpeed, supportedSleepModes = supportedSleepMode,
            roomTemp = 82, temp = 77
        )

        val state2 = getDeviceState().copy(
            supportedModes = supportedModes,
            supportedSpeeds = supportedFanSpeed, supportedSleepModes = supportedSleepMode,
            roomTemp = 79, temp = 75
        )
        
        return mutableMapOf(
            "A001" to state1,
            "A002" to state2
        )
    }
}