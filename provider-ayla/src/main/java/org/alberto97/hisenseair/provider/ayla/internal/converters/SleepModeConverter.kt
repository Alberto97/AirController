package org.alberto97.hisenseair.provider.ayla.internal.converters

import org.alberto97.hisenseair.provider.ayla.internal.models.Datapoint
import org.alberto97.hisenseair.provider.ayla.internal.models.Property
import org.alberto97.hisenseair.common.features.SleepMode

interface ISleepModeConverter : AylaConverter<SleepMode>

class SleepModeConverter : ISleepModeConverter {
    override fun map(data: Property): SleepMode {
        val double = data.value as Double
        return when(double.toInt()) {
            0 -> SleepMode.OFF
            1 -> SleepMode.MODE1
            2 -> SleepMode.MODE2
            3 -> SleepMode.MODE3
            4 -> SleepMode.MODE4
            else -> throw Exception("ayla: Unknown sleep mode")
        }
    }

    override fun map(data: SleepMode): Datapoint {
        val int = when(data) {
            SleepMode.OFF -> 0
            SleepMode.MODE1 -> 1
            SleepMode.MODE2 -> 2
            SleepMode.MODE3 -> 3
            SleepMode.MODE4 -> 4
        }
        return Datapoint(int)
    }

}