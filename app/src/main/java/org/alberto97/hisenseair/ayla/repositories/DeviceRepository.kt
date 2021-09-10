package org.alberto97.hisenseair.ayla.repositories

import android.util.Log
import org.alberto97.hisenseair.ayla.AylaExtensions.isAvailable
import org.alberto97.hisenseair.ayla.models.Device
import org.alberto97.hisenseair.ayla.models.ProductName
import org.alberto97.hisenseair.ayla.models.ProductNameWrapper
import org.alberto97.hisenseair.ayla.network.api.AylaService
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.models.ResultWrapper
import org.alberto97.hisenseair.repositories.IDeviceRepository

class DeviceRepository(private val service: AylaService) : IDeviceRepository {
    companion object {
        private const val LOG_TAG = "HiDevice"
    }

    override suspend fun getDevices(): ResultWrapper<List<AppDevice>> {
        return try {
            val devicesWrapperList = service.getDevices()
            val devices = devicesWrapperList.map { mapDeviceData(it.device) }
            ResultWrapper.Success(devices)
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.stackTraceToString())
            ResultWrapper.Error("Cannot retrieve devices data")
        }
    }

    override suspend fun getDevice(dsn: String): ResultWrapper<AppDevice> {
        return try {
            val resp = service.getDevice(dsn)
            val data = mapDeviceData(resp.device)
            ResultWrapper.Success(data)
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.stackTraceToString())
            ResultWrapper.Error("Cannot retrieve device data")
        }
    }

    override suspend fun setDeviceName(name: String, dsn: String): ResultWrapper<Unit> {
        val x = ProductName(name)
        val c = ProductNameWrapper(x)
        return try {
            service.putDevice(dsn, c)
            ResultWrapper.Success(Unit)
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.stackTraceToString())
            ResultWrapper.Error("Cannot update device name")
        }
    }

    override suspend fun deleteDevice(dsn: String): ResultWrapper<Unit> {
        val device = try {
             service.getDevice(dsn)
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.stackTraceToString())
            return ResultWrapper.Error("Cannot retrieve device key")
        }

        val key = device.device.key
        val resp = service.deleteDevice(key)
        return if (resp.isSuccessful) {
            ResultWrapper.Success(Unit)
        } else {
            Log.e(LOG_TAG, resp.code().toString())
            ResultWrapper.Error("Cannot delete device")
        }

    }

    private fun mapDeviceData(it: Device): AppDevice {
        return AppDevice(it.dsn, it.productName, it.isAvailable(), it.lanIp, it.mac, it.ssid)
    }
}