package org.alberto97.hisenseair.ayla.repositories

import android.util.Log
import org.alberto97.hisenseair.ayla.internal.AylaExtensions.isAvailable
import org.alberto97.hisenseair.ayla.features.TEMP_TYPE_PROP
import org.alberto97.hisenseair.ayla.internal.converters.ITempUnitConverter
import org.alberto97.hisenseair.ayla.internal.models.Device
import org.alberto97.hisenseair.ayla.internal.models.ProductName
import org.alberto97.hisenseair.ayla.internal.models.ProductNameWrapper
import org.alberto97.hisenseair.ayla.internal.network.api.AylaService
import org.alberto97.hisenseair.ayla.internal.repositories.IDeviceCacheRepository
import org.alberto97.hisenseair.ayla.internal.repositories.IDevicePropertyRepository
import org.alberto97.hisenseair.features.TempType
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.models.ResultWrapper
import org.alberto97.hisenseair.repositories.IDeviceRepository

class DeviceRepository(
    private val service: AylaService,
    private val propertyRepo: IDevicePropertyRepository,
    private val prefs: IDeviceCacheRepository,
    private val tempUnitConverter: ITempUnitConverter,
) : IDeviceRepository {
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
            prefs.setDeviceKey(dsn, resp.device.key)
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
        val key = prefs.getDeviceKey(dsn)
        val resp = service.deleteDevice(key)
        return if (resp.isSuccessful) {
            ResultWrapper.Success(Unit)
        } else {
            Log.e(LOG_TAG, resp.code().toString())
            ResultWrapper.Error("Cannot delete device")
        }

    }

    override suspend fun getTempUnit(dsn: String): ResultWrapper<TempType> {
        return try {
            val value = propertyRepo.getProperty(TEMP_TYPE_PROP, dsn)
            val unit = tempUnitConverter.map(value)
            prefs.setTempUnit(dsn, unit)
            ResultWrapper.Success(unit)
        } catch (e: Exception) {
            ResultWrapper.Error("Cannot get temperature unit")
        }
    }

    override suspend fun setTempUnit(dsn: String, value: TempType): ResultWrapper<Unit> {
        return try {
            prefs.setTempUnit(dsn, value)
            val datapoint = tempUnitConverter.map(value)
            propertyRepo.setProperty(dsn, TEMP_TYPE_PROP, datapoint)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error("Cannot set temperature unit")
        }
    }

    private fun mapDeviceData(it: Device): AppDevice {
        return AppDevice(it.dsn, it.productName, it.isAvailable(), it.lanIp, it.mac, it.ssid)
    }
}