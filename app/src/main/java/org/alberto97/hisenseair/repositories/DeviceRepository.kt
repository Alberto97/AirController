package org.alberto97.hisenseair.repositories

import org.alberto97.hisenseair.AylaService
import org.alberto97.hisenseair.ayla.AylaExtensions.isAvailable
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.ayla.models.Device
import org.alberto97.hisenseair.ayla.models.ProductName
import org.alberto97.hisenseair.ayla.models.ProductNameWrapper

interface IDeviceRepository {
    suspend fun getDevices(): List<AppDevice>
    suspend fun getDevice(dsn: String): AppDevice
    suspend fun setDeviceName(name: String, dsn: String)
}

class DeviceRepository(private val service: AylaService) : IDeviceRepository {

    override suspend fun getDevices(): List<AppDevice> {
        val devicesWrapperList = service.getDevices()
        return devicesWrapperList.map { mapDeviceData(it.device) }
    }

    override suspend fun getDevice(dsn: String): AppDevice {
        val resp = service.getDevice(dsn)
        return mapDeviceData(resp.device)
    }

    override suspend fun setDeviceName(name: String, dsn: String) {
        val x = ProductName(name)
        val c = ProductNameWrapper(x)
        service.putDevice(dsn, c)
    }

    private fun mapDeviceData(it: Device): AppDevice {
        return AppDevice(it.dsn, it.productName, it.isAvailable(), it.lanIp, it.mac, it.ssid)
    }
}