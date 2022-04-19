package org.alberto97.aircontroller.provider.ayla.internal.converters

import org.alberto97.aircontroller.provider.ayla.internal.models.BooleanProperty
import org.alberto97.aircontroller.provider.ayla.internal.models.Datapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.IntDatapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.Property

internal interface IBooleanConverter : AylaConverter<Boolean>

internal class BooleanConverter : IBooleanConverter {
    override fun map(data: Property): Boolean {
        val property = data as BooleanProperty
        return property.value ?: false
    }

    override fun map(data: Boolean): Datapoint {
        val value = if (data) 1 else 0
        return IntDatapoint(value)
    }
}