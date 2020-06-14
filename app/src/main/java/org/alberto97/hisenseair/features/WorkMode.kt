package org.alberto97.hisenseair.features

import android.os.Build
import android.service.controls.templates.TemperatureControlTemplate
import androidx.annotation.RequiresApi
import org.alberto97.hisenseair.R

enum class WorkMode(val value: Int) {
    FanOnly(0),
    Heating(1),
    Cooling(2),
    Dry(3),
    Auto(4);

    companion object {
        private val map = values().associateBy(WorkMode::value)
        fun from(type: Int) = map[type]
    }
}

val modeToIconMap = mapOf(
    WorkMode.Heating to R.drawable.round_brightness_7_24,
    WorkMode.Cooling to R.drawable.ic_weather_windy,
    WorkMode.Dry to R.drawable.water_outline,
    WorkMode.FanOnly to R.drawable.ic_fan,
    WorkMode.Auto to R.drawable.ic_air_conditioner
)

val modeToStringMap = mapOf(
    WorkMode.Heating to R.string.work_mode_heating,
    WorkMode.Cooling to R.string.work_mode_cooling,
    WorkMode.Dry to R.string.work_mode_dry,
    WorkMode.FanOnly to R.string.work_mode_fan_only,
    WorkMode.Auto to R.string.work_mode_auto
)

@RequiresApi(Build.VERSION_CODES.R)
val modeToControl = mapOf(
    WorkMode.Heating to TemperatureControlTemplate.MODE_HEAT,
    WorkMode.Cooling to TemperatureControlTemplate.MODE_COOL,
    WorkMode.Auto to TemperatureControlTemplate.MODE_HEAT_COOL,
    WorkMode.Dry to TemperatureControlTemplate.MODE_COOL,
    WorkMode.FanOnly to TemperatureControlTemplate.MODE_COOL
)

