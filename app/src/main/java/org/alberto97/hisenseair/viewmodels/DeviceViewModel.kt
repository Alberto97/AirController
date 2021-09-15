package org.alberto97.hisenseair.viewmodels

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.UriConstants
import org.alberto97.hisenseair.features.*
import org.alberto97.hisenseair.features.controllers.*
import org.alberto97.hisenseair.models.AppDeviceState
import org.alberto97.hisenseair.models.ListItemModel
import org.alberto97.hisenseair.repositories.IDeviceControlRepository
import org.alberto97.hisenseair.ui.MainActivity

class DeviceViewModel(
    private val dsn: String,
    private val repo: IDeviceControlRepository,
    private val context: Context,
    private val airFlowHorizontalController: IAirFlowHorizontalController,
    private val airFlowVerticalController: IAirFlowVerticalController,
    private val backlightController: IBacklightController,
    private val boostController: IBoostController,
    private val ecoController: IEcoController,
    private val fanSpeedController: IFanSpeedController,
    private val maxTempController: IMaxTempController,
    private val minTempController: IMinTempController,
    private val modeController: IModeController,
    private val powerController: IPowerController,
    private val quietController: IQuietController,
    private val roomTempController: IRoomTempController,
    private val sleepModeController: ISleepModeController,
    private val supportedFanSpeedController: ISupportedFanSpeedController,
    private val supportedModesController: ISupportedModesController,
    private val supportedSleepModesController: ISupportedSleepModesController,
    private val tempController: ITempControlController
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _workMode = MutableStateFlow(WorkMode.Auto)
    val workMode = _workMode.asStateFlow()

    private val _sleepMode = MutableStateFlow<SleepMode?>(SleepMode.OFF)
    val sleepMode = _sleepMode.asStateFlow()

    private val _fanSpeed = MutableStateFlow<FanSpeed?>(FanSpeed.Auto)
    val fanSpeed = _fanSpeed.asStateFlow()

    private val _backlight = MutableStateFlow<Boolean?>(true)
    val backlight = _backlight.asStateFlow()

    private val _horizontalAirFlow = MutableStateFlow<Boolean?>(false)
    val horizontalAirFlow = _horizontalAirFlow.asStateFlow()

    private val _verticalAirFlow = MutableStateFlow<Boolean?>(false)
    val verticalAirFlow = _verticalAirFlow.asStateFlow()

    private val _isQuiet = MutableStateFlow<Boolean?>(false)
    val isQuiet = _isQuiet.asStateFlow()

    private val _isEco = MutableStateFlow<Boolean?>(false)
    val isEco = _isEco.asStateFlow()

    private val _isBoost = MutableStateFlow<Boolean?>(false)
    val isBoost = _isBoost.asStateFlow()

    private val _isOn = MutableStateFlow<Boolean?>(false)
    val isOn = _isOn.asStateFlow()

    private val _roomTemp = MutableStateFlow(0)
    val roomTemp = _roomTemp.asStateFlow()

    private val _temp = MutableStateFlow<Int?>(0)
    val temp = _temp.asStateFlow()

    private val _maxTemp = MutableStateFlow<Int?>(0)
    val maxTemp = _maxTemp.asStateFlow()

    private val _minTemp = MutableStateFlow<Int?>(0)
    val minTemp = _minTemp.asStateFlow()

    private val _supportedModes = MutableStateFlow<List<WorkMode>>(emptyList())
    val supportedModes = combine(_supportedModes, workMode) { modes, mode ->
        modes.map { item ->
            ListItemModel(
                value = item,
                label = context.resources.getString(modeToStringMap.getValue(item)),
                resourceDrawable = modeToIconMap.getValue(item),
                selected = item == mode
            )
        }
    }

    private val _supportedFanSpeeds = MutableStateFlow<List<FanSpeed>>(emptyList())
    val supportedFanSpeeds = combine(_supportedFanSpeeds, fanSpeed) { speeds, speed ->
        speeds.map { item ->
            ListItemModel(
                value = item,
                label = context.resources.getString(fanToStringMap.getValue(item)),
                resourceDrawable = R.drawable.ic_fan,
                selected = item == speed
            )
        }
    }

    // TODO: This *really* needs to be improved by showing a graph
    //  or something else to visually differentiate the modes
    private val _supportedSleepModes = MutableStateFlow<List<SleepModeData>>(emptyList())
    val supportedSleepModes = combine(_supportedSleepModes, sleepMode) { modes, mode ->
        modes.map { item ->
            ListItemModel(
                value = item.type,
                label = context.resources.getString(sleepToStringMap.getValue(item.type)),
                resourceDrawable = R.drawable.ic_nights_stay,
                selected = item.type == mode
            )
        }
    }

    // Info
    private val _deviceName = MutableStateFlow("")
    val deviceName = _deviceName.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            fetchData()
            _isLoading.value = false
        }
    }

    private suspend fun fetchData() {
        val resp = repo.getDeviceState(dsn)
        withContext(Dispatchers.Main) {
            _deviceName.value = resp.productName
            _backlight.value = backlightController.getValue(resp)
            _workMode.value = modeController.getValue(resp)!!
            _horizontalAirFlow.value = airFlowHorizontalController.getValue(resp)
            _verticalAirFlow.value = airFlowVerticalController.getValue(resp)
            _isQuiet.value = quietController.getValue(resp)
            _isEco.value = ecoController.getValue(resp)
            _isBoost.value = boostController.getValue(resp)
            _sleepMode.value = sleepModeController.getValue(resp)
            _fanSpeed.value = fanSpeedController.getValue(resp)
            _temp.value = tempController.getValue(resp)
            _roomTemp.value = roomTempController.getValue(resp)!!
            _isOn.value = powerController.getValue(resp)
            _maxTemp.value = maxTempController.getValue(resp)
            _minTemp.value = minTempController.getValue(resp)
            _supportedFanSpeeds.value = supportedFanSpeedController.getValue(resp)!!
            _supportedModes.value = supportedModesController.getValue(resp)!!
            _supportedSleepModes.value = supportedSleepModesController.getValue(resp)!!
            createShortcut(resp)
        }
    }


    private fun createShortcut(device: AppDeviceState) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "${UriConstants.DEVICE_CONTROL}/$dsn".toUri(),
            context,
            MainActivity::class.java
        )

        val shortcut = ShortcutInfoCompat.Builder(context, dsn)
            .setShortLabel(device.productName)
            .setLongLabel(device.productName)
            .setRank(0)
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_fan_primary))
            .setIntent(intent)
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
    }

    private fun setProp(setProperty: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            setProperty()
            fetchData()
        }
    }

    private fun switchProp(data: MutableStateFlow<Boolean?>,
                           postFetch: (suspend () -> Unit)? = null,
                           setProperty: suspend (value: Boolean) -> Unit) {
        val opposite = !data.value!!
        data.value = opposite

        viewModelScope.launch(Dispatchers.IO) {
            setProperty(opposite)
            fetchData()
            if (postFetch != null)
                postFetch()
        }
    }

    fun setMode(mode: WorkMode) {
        setProp {
            repo.setMode(dsn, mode)
        }
    }

    fun setSleepMode(mode: SleepMode) {
        setProp {
            repo.setSleepMode(dsn, mode)
        }
    }

    fun setFanSpeed(speed: FanSpeed) {
        setProp {
            repo.setFanSpeed(dsn, speed)
        }
    }

    fun switchBacklight() {
        switchProp(_backlight) {
            repo.setBacklight(dsn, it)
        }
    }

    fun switchAirFlowHorizontal() {
        switchProp(_horizontalAirFlow) {
            repo.setAirFlowHorizontal(dsn, it)
        }
    }

    fun switchAirFlowVertical() {
        switchProp(_verticalAirFlow) {
            repo.setAirFlowVertical(dsn, it)
        }
    }

    fun switchQuiet() {
        switchProp(_isQuiet) {
            repo.setQuiet(dsn, it)
        }
    }

    fun switchEco() {
        switchProp(_isEco) {
            repo.setEco(dsn, it)
        }
    }

    fun switchBoost() {
        switchProp(_isBoost) {
            repo.setBoost(dsn, it)
        }
    }

    fun switchPower() {
        _isLoading.value = true
        switchProp(
            _isOn,
            setProperty = {
                repo.setPower(dsn, it)
            },
            postFetch = {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        )
    }

    fun setTemp(value: Int) {
        setProp {
            repo.setTemp(dsn, value)
        }
    }

    fun increaseTemp() = setTemp(temp.value!! + 1)

    fun reduceTemp() = setTemp(temp.value!! - 1)
}