package org.alberto97.hisenseair.ayla.internal.converters

import org.alberto97.hisenseair.ayla.internal.models.Datapoint
import org.alberto97.hisenseair.ayla.internal.models.Property

interface IIntConverter : AylaConverter<Int>

class IntConverter : IIntConverter {
    override fun map(data: Property): Int {
        val double = data.value as Double
        return double.toInt()
    }

    override fun map(data: Int): Datapoint {
        return Datapoint(data)
    }
}