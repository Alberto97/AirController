package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.features.*
import org.alberto97.hisenseair.features.controllers.*
import org.alberto97.hisenseair.models.BottomSheetListItem
import org.alberto97.hisenseair.repositories.IDeviceControlRepository

class DeviceViewModel(
    private val dsn: String,
    private val repo: IDeviceControlRepository,
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

    val supportedModes: MutableLiveData<List<WorkMode>> by lazy {
        MutableLiveData<List<WorkMode>>()
    }

    val supportedFanSpeeds: MutableLiveData<List<FanSpeed>> by lazy {
        MutableLiveData<List<FanSpeed>>()
    }

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
            supportedFanSpeeds.value = supportedFanSpeedController.getValue(resp)
            supportedModes.value = supportedModesController.getValue(resp)
        }
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

    fun getSupportedModes(): List<BottomSheetListItem<WorkMode>> {
        val modes = supportedModes.value ?: return listOf()

        return modes.map {
            val text = modeToStringMap.getValue(it)
            val icon = modeToIconMap.getValue(it)
            val selected = it == workState.value
            BottomSheetListItem(it, text, icon, selected)
        }
    }

    fun getSupportedFanSpeed(): List<BottomSheetListItem<FanSpeed>> {
        val modes = supportedFanSpeeds.value ?: return listOf()

        return modes.map {
            val text = fanToStringMap.getValue(it)
            val icon = R.drawable.ic_fan
            val selected = it == fanSpeed.value
            BottomSheetListItem(it, text, icon, selected)
        }
    }
}