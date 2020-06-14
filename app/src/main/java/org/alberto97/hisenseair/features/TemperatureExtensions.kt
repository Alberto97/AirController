package org.alberto97.hisenseair.features

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

object TemperatureExtensions {

    fun Int.toF(): Int {
        val x = ((this.toDouble() * 9/5) + 32)
        return x.toBigDecimal().setScale(0, RoundingMode.HALF_UP).toInt()
    }

    fun Int.toC(): Int {
        val x =   (5 *(this - 32.0)) / 9.0
        return x.toBigDecimal().setScale(0, RoundingMode.HALF_UP).toInt()
    }
}