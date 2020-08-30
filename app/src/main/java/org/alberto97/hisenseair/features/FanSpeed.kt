package org.alberto97.hisenseair.features

import org.alberto97.hisenseair.R

enum class FanSpeed {
    Auto,
    Lower,
    Low,
    Normal,
    High,
    Higher
}

val fanToStringMap = mapOf(
    FanSpeed.Auto to R.string.fan_speed_auto,
    FanSpeed.Lower to R.string.fan_speed_lower,
    FanSpeed.Low to R.string.fan_speed_low,
    FanSpeed.Normal to R.string.fan_speed_normal,
    FanSpeed.High to R.string.fan_speed_high,
    FanSpeed.Higher to R.string.fan_speed_higher
)