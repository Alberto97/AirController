package org.alberto97.aircontroller.provider.ayla.internal.converters

import org.alberto97.aircontroller.common.features.WorkMode

internal interface IModeConverter : AylaConverter<WorkMode, Int>

internal class ModeConverter : IModeConverter {

    override fun fromAyla(value: Int): WorkMode {
        return when (value) {
            0 -> WorkMode.FanOnly
            1 -> WorkMode.Heating
            2 -> WorkMode.Cooling
            3 -> WorkMode.Dry
            4 -> WorkMode.Auto
            else -> throw Exception("ayla: Unknown mode")
        }
    }

    override fun toAyla(data: WorkMode): Int {
        return when (data) {
            WorkMode.FanOnly -> 0
            WorkMode.Heating -> 1
            WorkMode.Cooling -> 2
            WorkMode.Dry -> 3
            WorkMode.Auto -> 4
        }
    }
}