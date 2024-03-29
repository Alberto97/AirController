package org.alberto97.aircontroller.provider.ayla.internal.repositories

import org.alberto97.aircontroller.provider.ayla.internal.models.Datapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.Property
import org.alberto97.aircontroller.provider.ayla.internal.network.api.AylaService

internal interface IDevicePropertyRepository {
    suspend fun getProperties(dsn: String): List<Property>
    suspend fun getProperty(property: String, dsn: String): Property
    suspend fun setProperty(dsn: String, property: String, value: Datapoint)
}

/**
 * Shared repository used to get/set device properties
 */
internal class DevicePropertyRepository(private val service: AylaService) : IDevicePropertyRepository {

    override suspend fun getProperties(dsn: String): List<Property> {
        val wrappedList = service.getDeviceProperties(dsn)
        return wrappedList.map { item -> item.property }
    }

    override suspend fun getProperty(property: String, dsn: String): Property {
        val wrappedValue = service.getDeviceProperty(dsn, property)
        return wrappedValue.property
    }

    override suspend fun setProperty(dsn: String, property: String, value: Datapoint) {
        service.setDeviceProperty(dsn, property, value)
    }
}