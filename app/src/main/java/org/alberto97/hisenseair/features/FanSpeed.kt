package org.alberto97.hisenseair.features

import org.alberto97.hisenseair.R

enum class FanSpeed(val value: Int) {
    Auto(0),
    Lower(5),
    Low(6),
    Normal(7),
    High(8),
    Higher(9);

    companion object {
        private val map = values().associateBy(FanSpeed::value)
        fun from(type: Int): FanSpeed {
            if (type == 1)
                return Lower

            return map.getValue(type)
        }
    }
}

val fanToStringMap = mapOf(
    FanSpeed.Auto to R.string.fan_speed_auto,
    FanSpeed.Lower to R.string.fan_speed_lower,
    FanSpeed.Low to R.string.fan_speed_low,
    FanSpeed.Normal to R.string.fan_speed_normal,
    FanSpeed.High to R.string.fan_speed_high,
    FanSpeed.Higher to R.string.fan_speed_higher
)