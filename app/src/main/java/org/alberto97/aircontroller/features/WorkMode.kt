package org.alberto97.aircontroller.features

import android.os.Build
import android.service.controls.templates.TemperatureControlTemplate
import androidx.annotation.RequiresApi
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.common.features.WorkMode

val modeToIconMap = mapOf(
    WorkMode.Heating to R.drawable.outline_brightness_low,
    WorkMode.Cooling to R.drawable.ic_weather_windy,
    WorkMode.Dry to R.drawable.outline_water_drop,
    WorkMode.FanOnly to R.drawable.ic_fan,
    WorkMode.Auto to R.drawable.outline_thermostat_auto
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

