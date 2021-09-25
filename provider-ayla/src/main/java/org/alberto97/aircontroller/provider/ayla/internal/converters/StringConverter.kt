package org.alberto97.aircontroller.provider.ayla.internal.converters

import org.alberto97.aircontroller.provider.ayla.internal.models.Datapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.Property

internal interface IStringConverter : AylaConverter<String>

internal class StringConverter : IStringConverter {
    override fun map(data: Property): String {
        return data.value as String
    }

    override fun map(data: String): Datapoint {
        throw NotImplementedError()
    }
}