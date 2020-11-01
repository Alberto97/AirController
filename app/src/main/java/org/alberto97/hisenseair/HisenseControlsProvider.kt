package org.alberto97.hisenseair

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.controls.Control
import android.service.controls.ControlsProviderService
import android.service.controls.DeviceTypes
import android.service.controls.actions.ControlAction
import android.service.controls.actions.FloatAction
import android.service.controls.templates.ControlTemplate
import android.service.controls.templates.RangeTemplate
import android.service.controls.templates.StatelessTemplate
import android.service.controls.templates.TemperatureControlTemplate
import androidx.annotation.RequiresApi
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.jdk9.asPublisher
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.features.modeToControl
import org.alberto97.hisenseair.features.modeToStringMap
import org.alberto97.hisenseair.models.AppDeviceState
import org.alberto97.hisenseair.repositories.IDeviceControlRepository
import org.alberto97.hisenseair.repositories.IDeviceRepository
import org.alberto97.hisenseair.ui.MainActivity
import org.koin.android.ext.android.inject
import java.util.concurrent.Flow
import java.util.function.Consumer

@RequiresApi(Build.VERSION_CODES.R)
class HisenseControlsProvider : ControlsProviderService() {

    private val device: IDeviceRepository by inject()
    private val deviceControl: IDeviceControlRepository by inject()

    override fun createPublisherForAllAvailable(): Flow.Publisher<Control> {
        return flow {
            val devices = device.getDevices()
            devices.forEach {
                val control = Control.StatelessBuilder(it.id, getPendingIntent(it.id))
                    .setTitle(it.name)
                    .setDeviceType(DeviceTypes.TYPE_THERMOSTAT)
                    .build()
                emit(control)
            }
        }.flowOn(Dispatchers.IO).asPublisher()
    }

    override fun createPublisherFor(controlIds: MutableList<String>): Flow.Publisher<Control> {
        return flow {
            controlIds.forEach {
                val status = getDeviceStatus(it)
                val control = createControlById(it, status)
                emit(control)
            }
        }.flowOn(Dispatchers.IO).asPublisher()
    }

    private suspend fun createControlById(dsn: String, status: Int): Control {
        val state = deviceControl.getDeviceState(dsn)
        val template = createThermostatTemplate(state)
        val statusText = getStatusText(state.on, state.workMode)
        return Control.StatefulBuilder(dsn, getPendingIntent(dsn))
            .setTitle(state.productName)
            .setSubtitle(getString(R.string.device_current_temp, state.roomTemp))
            .setDeviceType(DeviceTypes.TYPE_THERMOSTAT)
            .setControlTemplate(template)
            .setStatus(status)
            .setStatusText(statusText)
            .build()
    }

    private suspend fun getDeviceStatus(dsn: String): Int {
        val device = device.getDevice(dsn)
        return if (device.available)
            Control.STATUS_OK
        else
            Control.STATUS_DISABLED
    }

    override fun performControlAction(controlId: String, action: ControlAction, consumer: Consumer<Int>) {
        val ioScope = CoroutineScope(Dispatchers.IO + Job())

        if (action is FloatAction) {
            consumer.accept(ControlAction.RESPONSE_OK)
            val newTemp = action.newValue.toInt()
            ioScope.launch {
                deviceControl.setTemp(controlId, newTemp)
            }
        }
    }

    private fun getStatusText(isOn: Boolean, mode: WorkMode): String {
        val statusTextOn = getString(modeToStringMap[mode] ?: error(""))
        val statusTextOff = "Off"
        return if (isOn) statusTextOn else statusTextOff
    }

    private fun createThermostatTemplate(state: AppDeviceState): ControlTemplate {
        val currentTemp = state.temp.toFloat()
        val maxTemp = state.maxTemp.toFloat()
        val minTemp = state.minTemp.toFloat()

        val currentMode = modeToControl[state.workMode] ?: TemperatureControlTemplate.MODE_UNKNOWN
        val currentActiveMode = if (state.on) currentMode else TemperatureControlTemplate.MODE_OFF
        val supportedModes = TemperatureControlTemplate.FLAG_MODE_COOL or
                TemperatureControlTemplate.FLAG_MODE_HEAT or
                TemperatureControlTemplate.FLAG_MODE_HEAT_COOL or
                TemperatureControlTemplate.FLAG_MODE_OFF

        val controlTemplate = when (state.workMode) {
            WorkMode.Cooling,
            WorkMode.Heating -> RangeTemplate("range", minTemp, maxTemp, currentTemp, 1.0f, "%.1f°")
            else -> StatelessTemplate("noop")
        }


        return TemperatureControlTemplate("template", controlTemplate, currentMode, currentActiveMode, supportedModes)
    }

    private fun getPendingIntent(dsn: String): PendingIntent {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "${UriConstants.DEVICE_CONTROL}/$dsn".toUri(),
            baseContext,
            MainActivity::class.java
        )

        val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(baseContext).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        return deepLinkPendingIntent!!
    }
}