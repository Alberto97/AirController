package org.alberto97.hisenseair.provider.ayla.internal.converters

import org.alberto97.hisenseair.provider.ayla.internal.models.Datapoint
import org.alberto97.hisenseair.provider.ayla.internal.models.Property
import org.alberto97.hisenseair.common.features.WorkMode

interface IModeConverter : AylaConverter<WorkMode>

class ModeConverter : IModeConverter {

    override fun map(data: Property): WorkMode {
        val double = data.value as Double
        return when(double.toInt()) {
            0 -> WorkMode.FanOnly
            1 -> WorkMode.Heating
            2 -> WorkMode.Cooling
            3 -> WorkMode.Dry
            4 -> WorkMode.Auto
            else -> throw Exception("ayla: Unknown mode")
        }
    }

    override fun map(data: WorkMode): Datapoint {
        val int = when(data) {
            WorkMode.FanOnly -> 0
            WorkMode.Heating -> 1
            WorkMode.Cooling -> 2
            WorkMode.Dry -> 3
            WorkMode.Auto -> 4
        }
        return Datapoint(int)
    }
}