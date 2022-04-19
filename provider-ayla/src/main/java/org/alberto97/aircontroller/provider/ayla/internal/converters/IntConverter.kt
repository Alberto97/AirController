package org.alberto97.aircontroller.provider.ayla.internal.converters

import org.alberto97.aircontroller.provider.ayla.internal.models.Datapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.IntDatapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.IntProperty
import org.alberto97.aircontroller.provider.ayla.internal.models.Property

internal interface IIntConverter : AylaConverter<Int>

internal class IntConverter : IIntConverter {
    override fun map(data: Property): Int {
        val property = data as IntProperty
        return property.value ?: 0
    }

    override fun map(data: Int): Datapoint {
        return IntDatapoint(data)
    }
}