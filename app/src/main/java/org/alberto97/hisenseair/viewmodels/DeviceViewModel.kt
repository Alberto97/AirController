package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.hisenseair.features.*
import org.alberto97.hisenseair.repositories.IDeviceRepository

class DeviceViewModel(private val repo: IDeviceRepository) : ViewModel() {

    var dsn: String = ""
    var useCelsius = false

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
            backlight.value = resp.backlight
            workState.value = resp.workMode
            horizontalAirFlow.value = resp.horizontalAirFlow
            verticalAirFlow.value = resp.verticalAirFlow
            isQuiet.value = resp.quiet
            isEco.value = resp.eco
            isBoost.value = resp.boost
            sleepMode.value = resp.sleepMode
            fanSpeed.value = resp.fanSpeed
            temp.value = resp.temp
            roomTemp.value = resp.roomTemp
            isOn.value = resp.on
            useCelsius = !resp.fahrenheit
        }
    }

    private fun setProperty(property: String, value: Int) {
        viewModelScope.launch {
            repo.setProperty(property, value, dsn)

            withContext(Dispatchers.IO) {
                fetchData()
            }
        }
    }

    fun setTemp(value: Int) {
        viewModelScope.launch {
            repo.setTemp(value, dsn, useCelsius)

            withContext(Dispatchers.IO) {
                fetchData()
            }
        }
    }

    private fun switchProp(property: String, liveData: MutableLiveData<Boolean>) {
        val opposite = !liveData.value!!
        setProperty(property, if (opposite) 1 else 0)
        liveData.value = opposite
    }

    fun setMode(mode: WorkMode) {
        setProperty(WORK_MODE_PROP, mode.value)
    }

    fun setSleepMode(mode: Int) {
        setProperty(SLEEP_MODE_PROP, mode)
    }

    fun setFanSpeed(speed: FanSpeed) {
        setProperty(FAN_SPEED_PROP, speed.value)
    }

    fun switchBacklight() {
        switchProp(BACKLIGHT_PROP, backlight)
    }

    fun switchAirFlowHorizontal() {
        switchProp(HORIZONTAL_AIR_FLOW_PROP, horizontalAirFlow)
    }

    fun switchAirFlowVertical() {
        switchProp(VERTICAL_AIR_FLOW_PROP, verticalAirFlow)
    }

    fun switchQuiet() {
        switchProp(QUIET_PROP, isQuiet)
    }

    fun switchEco() {
        switchProp(ECO_PROP, isEco)
    }

    fun switchBoost() {
        switchProp(BOOST_PROP, isBoost)
    }

    fun switchPower() {
        switchProp(POWER_PROP, isOn)
    }

    private fun increaseTemp(value: Int) {
        val newTmp = temp.value!! + value
        setTemp(newTmp)
    }

    fun increaseTemp() = increaseTemp(+1)

    fun reduceTemp() = increaseTemp(-1)
}