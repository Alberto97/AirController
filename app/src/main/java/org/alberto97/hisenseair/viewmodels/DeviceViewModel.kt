package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.hisenseair.features.*
import org.alberto97.hisenseair.repositories.IDeviceControlRepository

class DeviceViewModel(private val repo: IDeviceControlRepository) : ViewModel() {

    var dsn: String = ""

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
            maxTemp.value = resp.maxTemp
            minTemp.value = resp.minTemp
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