package org.alberto97.hisenseair.provider.ayla.internal.converters

import org.alberto97.hisenseair.provider.ayla.internal.models.Datapoint
import org.alberto97.hisenseair.provider.ayla.internal.models.Property

internal interface IIntConverter : AylaConverter<Int>

internal class IntConverter : IIntConverter {
    override fun map(data: Property): Int {
        val double = data.value as Double
        return double.toInt()
    }

    override fun map(data: Int): Datapoint {
        return Datapoint(data)
    }
}