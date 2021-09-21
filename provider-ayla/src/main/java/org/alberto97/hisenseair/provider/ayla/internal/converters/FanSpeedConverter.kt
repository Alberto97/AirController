package org.alberto97.hisenseair.provider.ayla.internal.converters

import org.alberto97.hisenseair.provider.ayla.internal.models.Datapoint
import org.alberto97.hisenseair.provider.ayla.internal.models.Property
import org.alberto97.hisenseair.common.features.FanSpeed

internal interface IFanSpeedConverter : AylaConverter<FanSpeed>

internal class FanSpeedConverter : IFanSpeedConverter {
    override fun map(data: Property): FanSpeed {
        val double = data.value as Double
        val value = double.toInt()

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

    override fun map(data: FanSpeed): Datapoint {
        val int = when (data) {
            FanSpeed.Auto -> 0
            FanSpeed.Lower -> 5
            FanSpeed.Low -> 6
            FanSpeed.Normal -> 7
            FanSpeed.High -> 8
            FanSpeed.Higher -> 9
        }
        return Datapoint(int)
    }
}