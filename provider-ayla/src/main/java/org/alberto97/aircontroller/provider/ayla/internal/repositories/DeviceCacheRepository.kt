package org.alberto97.aircontroller.provider.ayla.internal.repositories

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.alberto97.aircontroller.provider.ayla.internal.converters.ITempUnitConverter
import org.alberto97.aircontroller.common.features.TempType

internal interface IDeviceCacheRepository {
    fun getTempUnit(dsn: String): TempType
    fun setTempUnit(dsn: String, unit: TempType)
    fun getDeviceKey(dsn: String): Int
    fun setDeviceKey(dsn: String, key: Int)
}

internal object DevicePreferences {
    const val TEMP_UNIT = "TEMP_UNIT"
    const val DEVICE_KEY = "DEVICE_KEY"
}

internal class DeviceCacheRepository(
    private val app: Application,
    private val converter: ITempUnitConverter
) : IDeviceCacheRepository {

    override fun getTempUnit(dsn: String): TempType {
        val prefs = getPreferences(dsn)
        val data = prefs.getInt(DevicePreferences.TEMP_UNIT, 1)
        return converter.mapIntToUnit(data)
    }

    override fun setTempUnit(dsn: String, unit: TempType) {
        val value = converter.mapUnitToInt(unit)
        val prefs = getPreferences(dsn)
        prefs.edit {
            putInt(DevicePreferences.TEMP_UNIT, value)
        }
    }

    override fun getDeviceKey(dsn: String): Int {
        val prefs = getPreferences(dsn)
        return prefs.getInt(DevicePreferences.DEVICE_KEY, -1)
    }

    override fun setDeviceKey(dsn: String, key: Int) {
        val prefs = getPreferences(dsn)
        prefs.edit {
            putInt(DevicePreferences.DEVICE_KEY, key)
        }
    }

    private fun getPreferences(dsn: String): SharedPreferences {
        return app.getSharedPreferences(dsn, Context.MODE_PRIVATE)
    }
}