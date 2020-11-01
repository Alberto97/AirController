package org.alberto97.hisenseair.ayla.repositories

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.alberto97.hisenseair.ayla.features.converters.ITempUnitConverter
import org.alberto97.hisenseair.features.TempType

interface IDeviceCacheRepository {
    fun getTempUnit(dsn: String): TempType
    fun setTempUnit(dsn: String, unit: TempType)
}

object DevicePreferences {
    const val TEMP_UNIT = "TEMP_UNIT"
}

class DeviceCacheRepository(
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

    private fun getPreferences(dsn: String): SharedPreferences {
        return app.getSharedPreferences(dsn, Context.MODE_PRIVATE)
    }
}