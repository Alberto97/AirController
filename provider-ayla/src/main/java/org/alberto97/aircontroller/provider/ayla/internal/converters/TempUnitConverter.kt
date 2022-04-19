package org.alberto97.aircontroller.provider.ayla.internal.converters

import org.alberto97.aircontroller.provider.ayla.internal.models.Datapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.Property
import org.alberto97.aircontroller.common.features.TempType
import org.alberto97.aircontroller.provider.ayla.internal.models.BooleanProperty
import org.alberto97.aircontroller.provider.ayla.internal.models.IntDatapoint

internal interface ITempUnitConverter : AylaConverter<TempType> {
    fun mapIntToUnit(value: Int): TempType
    fun mapUnitToInt(value: TempType): Int
}

internal class TempUnitConverter : ITempUnitConverter {

    override fun map(data: Property): TempType {
        val property = data as BooleanProperty
        return when(property.value) {
            false -> TempType.Celsius
            true -> TempType.Fahrenheit
            else -> throw Exception("ayla: Unrecognized temperature unit")
        }
    }

    override fun map(data: TempType): Datapoint {
        val value = mapUnitToInt(data)
        return IntDatapoint(value)
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