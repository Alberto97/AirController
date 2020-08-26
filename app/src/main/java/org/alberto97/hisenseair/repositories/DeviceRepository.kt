package org.alberto97.hisenseair.repositories

import org.alberto97.hisenseair.AylaService
import org.alberto97.hisenseair.models.Device
import org.alberto97.hisenseair.models.ProductName
import org.alberto97.hisenseair.models.ProductNameWrapper

interface IDeviceRepository {
    suspend fun getDevices(): List<Device>
    suspend fun getDevice(dsn: String): Device
    suspend fun setDeviceName(name: String, dsn: String)
}

class DeviceRepository(private val service: AylaService) : IDeviceRepository {

    override suspend fun getDevices(): List<Device> {
        val devicesWrapperList = service.getDevices()
        return devicesWrapperList.map { it.device }
    }

    override suspend fun getDevice(dsn: String): Device {
        val resp = service.getDevice(dsn)
        return resp.device
    }

    override suspend fun setDeviceName(name: String, dsn: String) {
        val x = ProductName(name)
        val c = ProductNameWrapper(x)
        service.putDevice(dsn, c)
    }
}