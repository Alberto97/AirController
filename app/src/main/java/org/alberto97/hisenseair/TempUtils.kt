package org.alberto97.hisenseair

import org.alberto97.hisenseair.features.TempType
import kotlin.math.roundToInt

interface ITempUtils {
    fun getMax(type: TempType): Int
    fun getMin(type: TempType): Int
}

class TempUtils() : ITempUtils {

    override fun getMax(type: TempType): Int {
        return if (type == TempType.Celsius) 32 else 90
    }

    override fun getMin(type: TempType): Int {
        return if (type == TempType.Celsius) 16 else 61
    }

//    fun progressToTemp(progress: Float): Int {
//        return (((max-min) * progress) + min).toDouble().roundToInt()
//    }
//
//    fun tempToProgress(temp: Int): Double {
//        return ((temp.toDouble() - min) / (max - min)) * 100
//    }

}