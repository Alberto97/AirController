package org.alberto97.aircontroller.common.features

import java.math.RoundingMode

object TemperatureExtensions {

    fun TempType.isCelsius(): Boolean {
        return this == TempType.Celsius
    }

    fun Int.toF(): Int {
        val x = ((this.toDouble() * 9/5) + 32)
        return x.toBigDecimal().setScale(0, RoundingMode.HALF_UP).toInt()
    }

    fun Int.toC(): Int {
        val x =   (5 *(this - 32.0)) / 9.0
        return x.toBigDecimal().setScale(0, RoundingMode.HALF_UP).toInt()
    }
}