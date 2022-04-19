package org.alberto97.aircontroller.provider.ayla.internal.converters

import org.alberto97.aircontroller.common.features.TempType

internal interface ITempUnitConverter : AylaConverter<TempType, Boolean> {
    fun mapIntToUnit(value: Int): TempType
    fun mapUnitToInt(value: TempType): Int
}

internal class TempUnitConverter : ITempUnitConverter {

    override fun fromAyla(value: Boolean): TempType {
        return when(value) {
            false -> TempType.Celsius
            true -> TempType.Fahrenheit
        }
    }

    override fun toAyla(data: TempType): Boolean {
        return when (data) {
            TempType.Celsius -> false
            TempType.Fahrenheit -> true
        }
    }

    override fun mapIntToUnit(value: Int): TempType {
        return when(value) {
            0 -> TempType.Celsius
            1 -> TempType.Fahrenheit
            else -> throw Exception("ayla: Unrecognized temperature unit")
        }
    }

    override fun mapUnitToInt(value: TempType): Int {
        return when(value) {
            TempType.Celsius -> 0
            TempType.Fahrenheit -> 1
        }
    }
}