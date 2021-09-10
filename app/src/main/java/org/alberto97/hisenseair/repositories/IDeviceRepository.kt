package org.alberto97.hisenseair.repositories

import org.alberto97.hisenseair.models.AppDevice

interface IDeviceRepository {
    suspend fun getDevices(): List<AppDevice>
    suspend fun getDevice(dsn: String): AppDevice
    suspend fun setDeviceName(name: String, dsn: String)
    suspend fun deleteDevice(dsn: String)
}