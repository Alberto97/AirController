package org.alberto97.aircontroller.provider.ayla.internal.converters

internal interface AylaConverter<T, TAyla> {
    fun fromAyla(value: TAyla): T
    fun toAyla(data: T): TAyla
}