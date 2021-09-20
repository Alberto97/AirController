package org.alberto97.hisenseair.ayla.internal.converters

import org.alberto97.hisenseair.ayla.internal.models.Datapoint
import org.alberto97.hisenseair.ayla.internal.models.Property

interface AylaConverter<T> {
    fun map(data: Property): T
    fun map(data: T): Datapoint
}