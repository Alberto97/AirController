package org.alberto97.aircontroller.common.features

enum class SleepMode {
    OFF,
    MODE1,
    MODE2,
    MODE3,
    MODE4
}

class SleepModeData(val type: SleepMode, val hours: List<Int>, val temps: List<Int>)