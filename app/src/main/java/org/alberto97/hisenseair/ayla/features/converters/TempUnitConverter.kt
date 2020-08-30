package org.alberto97.hisenseair.ayla.features.converters

import org.alberto97.hisenseair.ayla.models.Datapoint
import org.alberto97.hisenseair.ayla.models.Property
import org.alberto97.hisenseair.features.TempType

interface ITempUnitConverter : AylaConverter<TempType> {
    fun mapIntToUnit(value: Int): TempType
    fun mapUnitToInt(value: TempType): Int
}

class TempUnitConverter : ITempUnitConverter {

    override fun map(data: Property): TempType {
        val double = data.value as Double
        val value = double.toInt()
        return mapIntToUnit(value)
    }

    override fun map(data: TempType): Datapoint {
        val value = mapUnitToInt(data)
        return Datapoint(value)
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