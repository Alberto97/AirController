package org.alberto97.hisenseair

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
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
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.common.features.WorkMode
import org.alberto97.hisenseair.features.modeToControl
import org.alberto97.hisenseair.features.modeToStringMap
import org.alberto97.hisenseair.common.models.AppDeviceState
import org.alberto97.hisenseair.provider.repositories.IDeviceControlRepository
import org.alberto97.hisenseair.provider.repositories.IDeviceRepository
import org.alberto97.hisenseair.ui.MainActivity
import org.koin.android.ext.android.inject
import java.util.concurrent.Flow
import java.util.function.Consumer

@RequiresApi(Build.VERSION_CODES.R)
class HisenseControlsProvider : ControlsProviderService() {

    private val device: IDeviceRepository by inject()
    private val deviceControl: IDeviceControlRepository by inject()

    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private var updateSubscriber: Flow.Subscriber<in Control>? = null
    private val monitoredDevices = mutableListOf<String>()
    private val handler = Handler(Looper.getMainLooper())

    private val refresh = object : Runnable {
        override fun run() {
            monitoredDevices.forEach { deviceId ->
                ioScope.launch {
                    val status = getDeviceStatus(deviceId)
                    val control = createControlById(deviceId, status)
                    updateSubscriber?.onNext(control)
                }
            }
            handler.postDelayed(this, 2000)
        }
    }

    override fun createPublisherForAllAvailable(): Flow.Publisher<Control> {
        return Flow.Publisher { subscriber ->
            ioScope.launch {
                val devices = device.getDevices()
                devices.data?.forEach {
                    val control = Control.StatelessBuilder(it.id, getPendingIntent(it.id))
                        .setTitle(it.name)
                        .setDeviceType(DeviceTypes.TYPE_THERMOSTAT)
                        .build()
                    subscriber.onNext(control)
                }
                subscriber.onComplete()
            }
        }
    }

    override fun createPublisherFor(controlIds: MutableList<String>): Flow.Publisher<Control> {
        return Flow.Publisher { subscriber ->
            subscriber.onSubscribe(object : Flow.Subscription {
                override fun request(n: Long) {
                    updateSubscriber = subscriber
                }

                override fun cancel() {
                    updateSubscriber = null
                    handler.removeCallbacks(refresh)
                }
            })
            monitoredDevices.addAll(controlIds)
            handler.post(refresh)
        }
    }

    private suspend fun createControlById(dsn: String, status: Int): Control? {
        val result = deviceControl.getDeviceState(dsn)
        val state = result.data ?: return null
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
        val available = device.data?.available ?: false
        return if (available)
            Control.STATUS_OK
        else
            Control.STATUS_DISABLED
    }

    override fun performControlAction(controlId: String, action: ControlAction, consumer: Consumer<Int>) {
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
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        return deepLinkPendingIntent!!
    }
}