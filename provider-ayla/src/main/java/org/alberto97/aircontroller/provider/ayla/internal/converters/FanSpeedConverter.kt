package org.alberto97.aircontroller.provider.ayla.internal.converters

import org.alberto97.aircontroller.common.features.FanSpeed

internal interface IFanSpeedConverter : AylaConverter<FanSpeed, Int>

internal class FanSpeedConverter : IFanSpeedConverter {
    override fun fromAyla(value: Int): FanSpeed {
        if (value == 1) {
            // Quiet mode is on
            return FanSpeed.Lower
        }

        return when (value) {
            0 -> FanSpeed.Auto
            5 -> FanSpeed.Lower
            6 -> FanSpeed.Low
            7 -> FanSpeed.Normal
            8 -> FanSpeed.High
            9 -> FanSpeed.Higher
            else -> throw Exception("ayla: Unrecognized speed")
        }
    }

    override fun toAyla(data: FanSpeed): Int {
        return when (data) {
            FanSpeed.Auto -> 0
            FanSpeed.Lower -> 5
            FanSpeed.Low -> 6
            FanSpeed.Normal -> 7
            FanSpeed.High -> 8
            FanSpeed.Higher -> 9
        }
    }
}