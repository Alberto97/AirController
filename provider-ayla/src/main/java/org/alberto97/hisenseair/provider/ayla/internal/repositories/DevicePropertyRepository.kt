package org.alberto97.hisenseair.provider.ayla.internal.repositories

import org.alberto97.hisenseair.provider.ayla.internal.models.Datapoint
import org.alberto97.hisenseair.provider.ayla.internal.models.DatapointWrapper
import org.alberto97.hisenseair.provider.ayla.internal.models.Property
import org.alberto97.hisenseair.provider.ayla.internal.network.api.AylaService

internal interface IDevicePropertyRepository {
    suspend fun getProperty(property: String, dsn: String): Property
    suspend fun setProperty(dsn: String, property: String, value: Datapoint)
}

/**
 * Shared repository used to get/set device properties
 */
internal class DevicePropertyRepository(private val service: AylaService): IDevicePropertyRepository {

    override suspend fun getProperty(property: String, dsn: String): Property {
        val wrappedValue = service.getDeviceProperty(dsn, property)
        return wrappedValue.property
    }

    override suspend fun setProperty(dsn: String, property: String, value: Datapoint) {
        val data = DatapointWrapper(value)
        service.setDeviceProperty(dsn, property, data)
    }
}