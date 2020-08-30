package org.alberto97.hisenseair.ayla.features.converters

import org.alberto97.hisenseair.ayla.models.Datapoint
import org.alberto97.hisenseair.ayla.models.Property

interface AylaConverter<T> {
    fun map(data: Property): T
    fun map(data: T): Datapoint
}