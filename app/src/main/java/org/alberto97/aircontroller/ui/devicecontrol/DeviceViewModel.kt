package org.alberto97.aircontroller.ui.devicecontrol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.alberto97.aircontroller.common.features.*
import org.alberto97.aircontroller.common.models.AppDeviceState
import org.alberto97.aircontroller.common.models.ResultWrapper
import org.alberto97.aircontroller.models.ScreenState
import org.alberto97.aircontroller.provider.features.controllers.*
import org.alberto97.aircontroller.provider.repositories.IDeviceControlRepository
import org.alberto97.aircontroller.utils.IDeviceShortcutManager

class DeviceViewModel(
    private val dsn: String,
    private val repo: IDeviceControlRepository,
    private val shortcutManager: IDeviceShortcutManager,
    private val airFlowHorizontalController: IAirFlowHorizontalController,
    private val airFlowVerticalController: IAirFlowVerticalController,
    private val backlightTypeController: IBacklightTypeController,
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

    private val _state = MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

    private val _workMode = MutableStateFlow(WorkMode.Auto)
    val workMode = _workMode.asStateFlow()

    private val _sleepMode = MutableStateFlow<SleepMode?>(SleepMode.OFF)
    val sleepMode = _sleepMode.asStateFlow()

    private val _fanSpeed = MutableStateFlow<FanSpeed?>(FanSpeed.Auto)
    val fanSpeed = _fanSpeed.asStateFlow()

    private val _backlightType = MutableStateFlow(BacklightType.Stateful)
    val backlightType = _backlightType.asStateFlow()

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
    val supportedModes = _supportedModes.asStateFlow()

    private val _supportedFanSpeeds = MutableStateFlow<List<FanSpeed>>(emptyList())
    val supportedFanSpeeds = _supportedFanSpeeds.asStateFlow()

    // TODO: This *really* needs to be improved by showing a graph
    //  or something else to visually differentiate the modes
    private val _supportedSleepModes = MutableStateFlow<List<SleepModeData>>(emptyList())
    val supportedSleepModes = _supportedSleepModes.asStateFlow()

    // Info
    private val _deviceName = MutableStateFlow("")
    val deviceName = _deviceName.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ScreenState.Loading
            val result = fetchData()
            _state.value = if (result is ResultWrapper.Success)
                ScreenState.Success
            else
                ScreenState.Error
        }
    }

    private suspend fun fetchData(): ResultWrapper<AppDeviceState> {
        val resp = repo.getDeviceState(dsn)
        val data = resp.data
        if (data == null) {
            _message.value = resp.message
            return resp
        }

        updateUi(data)
        shortcutManager.createShortcut(data.productName, dsn)

        return resp
    }

    private fun updateUi(data: AppDeviceState) {
        _deviceName.value = data.productName
        _backlightType.value = backlightTypeController.getValue(data)!!
        _backlight.value = backlightController.getValue(data)
        _workMode.value = modeController.getValue(data)!!
        _horizontalAirFlow.value = airFlowHorizontalController.getValue(data)
        _verticalAirFlow.value = airFlowVerticalController.getValue(data)
        _isQuiet.value = quietController.getValue(data)
        _isEco.value = ecoController.getValue(data)
        _isBoost.value = boostController.getValue(data)
        _sleepMode.value = sleepModeController.getValue(data)
        _fanSpeed.value = fanSpeedController.getValue(data)
        _temp.value = tempController.getValue(data)
        _roomTemp.value = roomTempController.getValue(data)!!
        _isOn.value = powerController.getValue(data)
        _maxTemp.value = maxTempController.getValue(data)
        _minTemp.value = minTempController.getValue(data)
        _supportedFanSpeeds.value = supportedFanSpeedController.getValue(data)!!
        _supportedModes.value = supportedModesController.getValue(data)!!
        _supportedSleepModes.value = supportedSleepModesController.getValue(data)!!
    }

    fun clearMessage() {
        _message.value = ""
    }

    private suspend fun <T> setProp(setValue: suspend () -> ResultWrapper<T>) {
        val resp = setValue()
        if (resp is ResultWrapper.Success)
            fetchData()
        else
            _message.value = resp.message
    }

    private suspend fun <T> switchProp(value: Boolean, setValue: suspend (value: Boolean) -> ResultWrapper<T>) {
        val opposite = !value
        val resp = setValue(opposite)
        if (resp is ResultWrapper.Success)
            fetchData()
        else
            _message.value = resp.message
    }

    fun setMode(mode: WorkMode) {
        viewModelScope.launch(Dispatchers.IO) {
            setProp {
                repo.setMode(dsn, mode)
            }
        }
    }

    fun setSleepMode(mode: SleepMode) {
        viewModelScope.launch(Dispatchers.IO) {
            setProp {
                repo.setSleepMode(dsn, mode)
            }
        }
    }

    fun setFanSpeed(speed: FanSpeed) {
        viewModelScope.launch(Dispatchers.IO) {
            setProp {
                repo.setFanSpeed(dsn, speed)
            }
        }
    }

    fun setBacklight(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            setProp {
                repo.setBacklight(dsn, enabled)
            }
        }
    }

    fun switchBacklight() {
        viewModelScope.launch(Dispatchers.IO) {
            switchProp(
                value = _backlight.value!!,
                setValue = { value -> repo.setBacklight(dsn, value) }
            )
        }
    }

    fun switchAirFlowHorizontal() {
        viewModelScope.launch(Dispatchers.IO) {
            switchProp(
                value = _horizontalAirFlow.value!!,
                setValue = { value -> repo.setAirFlowHorizontal(dsn, value) }
            )
        }
    }

    fun switchAirFlowVertical() {
        viewModelScope.launch(Dispatchers.IO) {
            switchProp(
                value = _verticalAirFlow.value!!,
                setValue = { value -> repo.setAirFlowVertical(dsn, value) }
            )
        }
    }

    fun switchQuiet() {
        viewModelScope.launch(Dispatchers.IO) {
            switchProp(
                value = _isQuiet.value!!,
                setValue = { value -> repo.setQuiet(dsn, value) }
            )
        }
    }

    fun switchEco() {
        viewModelScope.launch(Dispatchers.IO) {
            switchProp(
                value = _isEco.value!!,
                setValue = { value -> repo.setEco(dsn, value) }
            )
        }
    }

    fun switchBoost() {
        viewModelScope.launch(Dispatchers.IO) {
            switchProp(
                value = _isBoost.value!!,
                setValue = { value -> repo.setBoost(dsn, value) }
            )
        }
    }

    fun switchPower() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ScreenState.Loading
            switchProp(
                value = _isOn.value!!,
                setValue = { value -> repo.setPower(dsn, value) },
            )

            // At this point it does not matter if this call fails,
            // there is enough data to populate the UI anyway
            _state.value = ScreenState.Success
        }
    }

    fun setTemp(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            setProp {
                repo.setTemp(dsn, value)
            }
        }
    }

    fun increaseTemp() = setTemp(temp.value!! + 1)

    fun reduceTemp() = setTemp(temp.value!! - 1)
}