package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.hisenseair.features.FanSpeed
import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.features.controllers.*
import org.alberto97.hisenseair.repositories.IDeviceControlRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class DeviceViewModel(private val repo: IDeviceControlRepository) : ViewModel(), KoinComponent {

    var dsn: String = ""

    private val airFlowHorizontalController: IAirFlowHorizontalController by inject()
    private val airFlowVerticalController: IAirFlowVerticalController by inject()
    private val backlightController: IBacklightController by inject()
    private val boostController: IBoostController by inject()
    private val ecoController: IEcoController by inject()
    private val fanSpeedController: IFanSpeedController by inject()
    private val maxTempController: IMaxTempController by inject()
    private val minTempController: IMinTempController by inject()
    private val modeController: IModeController by inject()
    private val powerController: IPowerController by inject()
    private val quietController: IQuietController by inject()
    private val roomTempController: IRoomTempController by inject()
    private val sleepModeController: ISleepModeController by inject()
    private val tempController: ITempControlController by inject()

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    // State
    val workState: MutableLiveData<WorkMode> by lazy {
        MutableLiveData<WorkMode>()
    }

    val sleepMode: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
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

    // Info
    val deviceName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun load(dsn: String) {
        this.dsn = dsn
        viewModelScope.launch {
            fetchData()
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
        }
    }

    private fun setProp(setProperty: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            setProperty()
            fetchData()
        }
    }

    private fun switchProp(liveData: MutableLiveData<Boolean>,
                           setProperty: suspend (value: Boolean) -> Unit) {
        val opposite = !liveData.value!!
        liveData.value = opposite

        viewModelScope.launch(Dispatchers.IO) {
            setProperty(opposite)
            fetchData()
        }
    }

    fun setMode(mode: WorkMode) {
        setProp {
            repo.setMode(dsn, mode)
        }
    }

    fun setSleepMode(mode: Int) {
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
        switchProp(isOn) {
            repo.setPower(dsn, it)
        }
    }

    fun setTemp(value: Int) {
        setProp {
            repo.setTemp(value, dsn)
        }
    }

    fun increaseTemp() = setTemp(temp.value!! + 1)

    fun reduceTemp() = setTemp(temp.value!! - 1)
}