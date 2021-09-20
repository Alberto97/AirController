package org.alberto97.hisenseair.ayla.internal.converters

import org.alberto97.hisenseair.ayla.internal.models.Datapoint
import org.alberto97.hisenseair.ayla.internal.models.Property

interface IBooleanConverter : AylaConverter<Boolean>

class BooleanConverter : IBooleanConverter {
    override fun map(data: Property): Boolean {
        val double = data.value as Double
        return double > 0
    }

    override fun map(data: Boolean): Datapoint {
        val value = if (data) 1 else 0
        return Datapoint(value)
    }
}