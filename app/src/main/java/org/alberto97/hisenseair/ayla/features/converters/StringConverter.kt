package org.alberto97.hisenseair.ayla.features.converters

import org.alberto97.hisenseair.ayla.models.Datapoint
import org.alberto97.hisenseair.ayla.models.Property

interface IStringConverter : AylaConverter<String>

class StringConverter : IStringConverter {
    override fun map(data: Property): String {
        return data.value as String
    }

    override fun map(data: String): Datapoint {
        throw NotImplementedError()
    }
}