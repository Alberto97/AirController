package org.alberto97.hisenseair.features

import org.alberto97.hisenseair.R

enum class SleepMode {
    OFF,
    MODE1,
    MODE2,
    MODE3,
    MODE4
}

class SleepModeData(val type: SleepMode, val hours: List<Int>, val temps: List<Int>)

val sleepToStringMap = mapOf(
    SleepMode.OFF to R.string.sleep_mode_off,
    SleepMode.MODE1 to R.string.sleep_mode_mode1,
    SleepMode.MODE2 to R.string.sleep_mode_mode2,
    SleepMode.MODE3 to R.string.sleep_mode_mode3,
    SleepMode.MODE4 to R.string.sleep_mode_mode4
)