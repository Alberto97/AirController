package org.alberto97.aircontroller.provider.ayla.internal.converters

import org.alberto97.aircontroller.provider.ayla.internal.models.Datapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.Property
import org.alberto97.aircontroller.common.features.SleepMode
import org.alberto97.aircontroller.provider.ayla.internal.models.IntDatapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.IntProperty

internal interface ISleepModeConverter : AylaConverter<SleepMode>

internal class SleepModeConverter : ISleepModeConverter {
    override fun map(data: Property): SleepMode {
        val property = data as IntProperty
        return when(property.value) {
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
        return IntDatapoint(int)
    }

}