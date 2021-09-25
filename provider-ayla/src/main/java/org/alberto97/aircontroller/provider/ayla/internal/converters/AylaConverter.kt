package org.alberto97.aircontroller.provider.ayla.internal.converters

import org.alberto97.aircontroller.provider.ayla.internal.models.Datapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.Property

internal interface AylaConverter<T> {
    fun map(data: Property): T
    fun map(data: T): Datapoint
}