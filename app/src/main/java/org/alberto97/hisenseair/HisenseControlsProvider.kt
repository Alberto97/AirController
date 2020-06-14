package org.alberto97.hisenseair

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.controls.Control
import android.service.controls.ControlsProviderService
import android.service.controls.DeviceTypes
import android.service.controls.actions.ControlAction
import android.service.controls.templates.*
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.jdk9.asPublisher
import org.alberto97.hisenseair.features.TempType
import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.features.modeToControl
import org.alberto97.hisenseair.features.modeToStringMap
import org.alberto97.hisenseair.repositories.IDeviceRepository
import org.koin.android.ext.android.inject
import java.util.concurrent.Flow
import java.util.function.Consumer

@RequiresApi(Build.VERSION_CODES.R)
class HisenseControlsProvider : ControlsProviderService() {

    val device: IDeviceRepository by inject()

    private val max: Float
        get() = if (TempType.Celsius == TempType.Celsius) 32.0f else 90.0f

    private val min: Float
        get() = if (TempType.Celsius == TempType.Celsius) 16.0f else 61.0f

    override fun createPublisherForAllAvailable(): Flow.Publisher<Control> {
        return flow {
            val devices = device.getDevices()
            devices.forEach {
                val control = Control.StatelessBuilder(it.dsn, getPendingIntent())
                    .setTitle(it.productName)
                    .setDeviceType(DeviceTypes.TYPE_THERMOSTAT)
                    .build()
                emit(control)
            }
        }.flowOn(Dispatchers.IO).asPublisher()
    }

    override fun createPublisherFor(controlIds: MutableList<String>): Flow.Publisher<Control> {
        return flow {
            controlIds.forEach {
                val state = device.getDeviceState(it)
                val device = device.getDevice(it)
                val status = if (device.connectionStatus == "Online") Control.STATUS_OK else Control.STATUS_DISABLED
                val template = createThermostatTemplate(state.on, state.workMode, state.temp)
                val statusText = getStatusText(state.on, state.workMode, state.temp)
                val control = Control.StatefulBuilder(it, getPendingIntent())
                    .setTitle(state.productName)
                    .setSubtitle(getString(R.string.device_ambient_temp, state.roomTemp))
                    .setDeviceType(DeviceTypes.TYPE_THERMOSTAT)
                    .setControlTemplate(template)
                    .setStatus(status)
                    .setStatusText(statusText)
                    .build()
                emit(control)
            }
        }.flowOn(Dispatchers.IO).asPublisher()
    }

    override fun performControlAction(controlId: String, action: ControlAction, consumer: Consumer<Int>) {
        //
        consumer.accept(ControlAction.RESPONSE_OK)
    }

    private fun getStatusText(isOn: Boolean, mode: WorkMode, temp: Int): String {
        val strMode = getString(modeToStringMap[mode] ?: error(""))
        val statusTextOn = "$strMode • ${temp}°"
        val statusTextOff = "Off"
        return if (isOn) statusTextOn else statusTextOff
    }

    private fun createThermostatTemplate(isOn: Boolean, mode: WorkMode, currentTemp: Int): ControlTemplate {
        val currentMode = modeToControl[mode] ?: TemperatureControlTemplate.MODE_UNKNOWN
        val currentActiveMode = if (isOn) currentMode else TemperatureControlTemplate.MODE_OFF
        val supportedModes = TemperatureControlTemplate.FLAG_MODE_COOL or
                TemperatureControlTemplate.FLAG_MODE_HEAT or
                TemperatureControlTemplate.FLAG_MODE_HEAT_COOL or
                TemperatureControlTemplate.FLAG_MODE_OFF

        val rt = RangeTemplate("range", min, max, currentTemp.toFloat(), 1.0f, null)
        return TemperatureControlTemplate("template", rt, currentMode, currentActiveMode, supportedModes)
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(
                baseContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
    }
}