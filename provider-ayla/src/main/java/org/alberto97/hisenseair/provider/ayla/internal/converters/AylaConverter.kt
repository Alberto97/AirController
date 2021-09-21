package org.alberto97.hisenseair.provider.ayla.internal.converters

import org.alberto97.hisenseair.provider.ayla.internal.models.Datapoint
import org.alberto97.hisenseair.provider.ayla.internal.models.Property

interface AylaConverter<T> {
    fun map(data: Property): T
    fun map(data: T): Datapoint
}