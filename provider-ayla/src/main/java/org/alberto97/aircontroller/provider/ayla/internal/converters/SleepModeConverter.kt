package org.alberto97.aircontroller.provider.ayla.internal.converters

import org.alberto97.aircontroller.common.features.SleepMode

internal interface ISleepModeConverter : AylaConverter<SleepMode, Int>

internal class SleepModeConverter : ISleepModeConverter {
    override fun fromAyla(value: Int): SleepMode {
        return when (value) {
            0 -> SleepMode.OFF
            1 -> SleepMode.MODE1
            2 -> SleepMode.MODE2
            3 -> SleepMode.MODE3
            4 -> SleepMode.MODE4
            else -> throw Exception("ayla: Unknown sleep mode")
        }
    }

    override fun toAyla(data: SleepMode): Int {
        return when (data) {
            SleepMode.OFF -> 0
            SleepMode.MODE1 -> 1
            SleepMode.MODE2 -> 2
            SleepMode.MODE3 -> 3
            SleepMode.MODE4 -> 4
        }
    }

}