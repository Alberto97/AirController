package org.alberto97.hisenseair.viewmodels

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
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

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    // State
    val workState: MutableLiveData<WorkMode> by lazy {
        MutableLiveData<WorkMode>()
    }

    val sleepMode: MutableLiveData<SleepMode> by lazy {
        MutableLiveData<SleepMode>()
    }

    val fanSpeed: MutableLiveData<FanSpeed> by lazy {
        MutableLiveData<FanSpeed>()
    }

    val backlight: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val horizontalAirFlow: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val verticalAirFlow: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isQuiet: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isEco: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isBoost: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isOn: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val roomTemp: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val temp: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val maxTemp: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val minTemp: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    private val _supportedModes: MutableLiveData<List<WorkMode>> by lazy {
        MutableLiveData<List<WorkMode>>()
    }

    val supportedModes = combine(_supportedModes.asFlow(), workState.asFlow()) { modes, mode ->
        modes.map { item ->
            ListItemModel(
                value = item,
                label = context.resources.getString(modeToStringMap.getValue(item)),
                resourceDrawable = modeToIconMap.getValue(item),
                selected = item == mode
            )
        }
    }.asLiveData()

    private val _supportedFanSpeeds: MutableLiveData<List<FanSpeed>> by lazy {
        MutableLiveData<List<FanSpeed>>()
    }

    val supportedFanSpeeds = combine(_supportedFanSpeeds.asFlow(), fanSpeed.asFlow()) { speeds, speed ->
        speeds.map { item ->
            ListItemModel(
                value = item,
                label = context.resources.getString(fanToStringMap.getValue(item)),
                resourceDrawable = R.drawable.ic_fan,
                selected = item == speed
            )
        }
    }.asLiveData()

    private val _supportedSleepModes: MutableLiveData<List<SleepModeData>> by lazy {
        MutableLiveData<List<SleepModeData>>()
    }

    // TODO: This *really* needs to be improved by showing a graph
    //  or something else to visually differentiate the modes
    val supportedSleepModes = combine(_supportedSleepModes.asFlow(), sleepMode.asFlow()) { modes, mode ->
        modes.map { item ->
            ListItemModel(
                value = item.type,
                label = context.resources.getString(sleepToStringMap.getValue(item.type)),
                resourceDrawable = R.drawable.ic_nights_stay,
                selected = item.type == mode
            )
        }
    }.asLiveData()

    // Info
    val deviceName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            fetchData()
            withContext(Dispatchers.Main) {
                isLoading.value = false
            }
        }
    }

    private suspend fun fetchData() {
        val resp = repo.getDeviceState(dsn)
        withContext(Dispatchers.Main) {
            deviceName.value = resp.productName
            backlight.value = backlightController.getValue(resp)
            workState.value = modeController.getValue(resp)
            horizontalAirFlow.value = airFlowHorizontalController.getValue(resp)
            verticalAirFlow.value = airFlowVerticalController.getValue(resp)
            isQuiet.value = quietController.getValue(resp)
            isEco.value = ecoController.getValue(resp)
            isBoost.value = boostController.getValue(resp)
            sleepMode.value = sleepModeController.getValue(resp)
            fanSpeed.value = fanSpeedController.getValue(resp)
            temp.value = tempController.getValue(resp)
            roomTemp.value = roomTempController.getValue(resp)
            isOn.value = powerController.getValue(resp)
            maxTemp.value = maxTempController.getValue(resp)
            minTemp.value = minTempController.getValue(resp)
            _supportedFanSpeeds.value = supportedFanSpeedController.getValue(resp)
            _supportedModes.value = supportedModesController.getValue(resp)
            _supportedSleepModes.value = supportedSleepModesController.getValue(resp)
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

    private fun switchProp(liveData: MutableLiveData<Boolean>,
                           postFetch: (suspend () -> Unit)? = null,
                           setProperty: suspend (value: Boolean) -> Unit) {
        val opposite = !liveData.value!!
        liveData.value = opposite

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
        switchProp(backlight) {
            repo.setBacklight(dsn, it)
        }
    }

    fun switchAirFlowHorizontal() {
        switchProp(horizontalAirFlow) {
            repo.setAirFlowHorizontal(dsn, it)
        }
    }

    fun switchAirFlowVertical() {
        switchProp(verticalAirFlow) {
            repo.setAirFlowVertical(dsn, it)
        }
    }

    fun switchQuiet() {
        switchProp(isQuiet) {
            repo.setQuiet(dsn, it)
        }
    }

    fun switchEco() {
        switchProp(isEco) {
            repo.setEco(dsn, it)
        }
    }

    fun switchBoost() {
        switchProp(isBoost) {
            repo.setBoost(dsn, it)
        }
    }

    fun switchPower() {
        isLoading.value = true
        switchProp(
            isOn,
            setProperty = {
                repo.setPower(dsn, it)
            },
            postFetch = {
                withContext(Dispatchers.Main) {
                    isLoading.value = false
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