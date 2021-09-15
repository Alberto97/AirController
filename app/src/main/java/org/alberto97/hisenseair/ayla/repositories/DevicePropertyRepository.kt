package org.alberto97.hisenseair.ayla.repositories

import org.alberto97.hisenseair.ayla.models.Datapoint
import org.alberto97.hisenseair.ayla.models.DatapointWrapper
import org.alberto97.hisenseair.ayla.models.Property
import org.alberto97.hisenseair.ayla.network.api.AylaService

interface IDevicePropertyRepository {
    suspend fun getProperty(property: String, dsn: String): Property
    suspend fun setProperty(dsn: String, property: String, value: Datapoint)
}

/**
 * Shared repository used to get/set device properties
 */
class DevicePropertyRepository(private val service: AylaService): IDevicePropertyRepository {

    override suspend fun getProperty(property: String, dsn: String): Property {
        val wrappedValue = service.getDeviceProperty(dsn, property)
        return wrappedValue.property
    }

    override suspend fun setProperty(dsn: String, property: String, value: Datapoint) {
        val data = DatapointWrapper(value)
        service.setDeviceProperty(dsn, property, data)
    }
}