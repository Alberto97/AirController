package org.alberto97.hisenseair.ui.devicecontrol

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.features.fanToStringMap
import org.alberto97.hisenseair.features.modeToIconMap
import org.alberto97.hisenseair.features.modeToStringMap
import org.alberto97.hisenseair.features.sleepToStringMap
import org.alberto97.hisenseair.ui.BottomSheetList
import org.alberto97.hisenseair.ui.FullscreenLoading
import org.alberto97.hisenseair.ui.preferences.Preference
import org.alberto97.hisenseair.ui.preferences.PreferenceCategory
import org.alberto97.hisenseair.ui.preferences.PreferenceDescription
import org.alberto97.hisenseair.ui.preferences.SwitchPreference
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

enum class DeviceControlSheet {
    None,
    FanSpeed,
    Mode,
    TempControl
}

@ExperimentalMaterialApi
@Composable
fun DeviceControlScreen(
    dsn: String,
    viewModel: DeviceViewModel = getViewModel { parametersOf(dsn) },
    displayInPanel: Boolean,
    onSettingsClick: () -> Unit
) {

    if (displayInPanel)
        Surface {
            PanelUnsupported()
        }
    else {
        val (sheetType, setSheetType ) = remember { mutableStateOf(DeviceControlSheet.None) }
        val sheetScope = rememberCoroutineScope()
        val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

        ModalBottomSheetLayout(
            sheetState = state,
            sheetContent = {
                sheetContent(
                    viewModel = viewModel,
                    currentSheet = sheetType,
                    close = { state.hide() }
                )
            }
        ){
            Scaffold(
                topBar = {
                    topAppBar(
                        viewModel = viewModel,
                        navigateToSettings = onSettingsClick
                    )
                }
            ) {
                val isLoading = viewModel.isLoading.observeAsState(true).value
                if (isLoading) {
                    FullscreenLoading()
                } else {
                    val isOn = viewModel.isOn.observeAsState(null)
                    when (isOn.value) {
                        false -> offScreen(viewModel)
                        true -> onScreen(
                            viewModel,
                            showSheet = {
                                setSheetType(it)
                                sheetScope.launch {
                                    delay(100L)
                                    state.show()
                                }
                            }
                        )
                        else -> FullscreenLoading()
                    }
                }
            }
        }

    }
}

@Composable
private fun topAppBar(
    viewModel: DeviceViewModel,
    navigateToSettings: () -> Unit
) {
    val deviceName = viewModel.deviceName.observeAsState().value ?: ""

    TopAppBar(
        title = { Text(deviceName) },
        actions = {
            IconButton({ navigateToSettings() }) {
                Icon(Icons.Rounded.Settings)
            }
        }
    )
}

@Composable
private fun offScreen(viewModel: DeviceViewModel) {
    val currentTemp by viewModel.roomTemp.observeAsState(-1)
    val currentMode by viewModel.workState.observeAsState()
    val drawableId = modeToIconMap[currentMode] ?: R.drawable.round_brightness_7_24

    DeviceOff(
        modeAsset = vectorResource(drawableId),
        currentTemp = currentTemp,
        onPower = { viewModel.switchPower() }
    )
}

@Composable
private fun fanSpeedSheet(
    viewModel: DeviceViewModel,
    close: () -> Unit
) {
    BottomSheetList(
        title = "Fan Speed",
        list = viewModel.getSupportedFanSpeed(),
        onItemClick = { data ->
            viewModel.setFanSpeed(data)
            close()
        }
    )
}

@Composable
private fun tempControlSheet(
    viewModel: DeviceViewModel,
    close: () -> Unit
) {
    TemperatureControlDialog(
        temp = viewModel.temp.value!!.toFloat(),
        min = viewModel.minTemp.value!!.toFloat(),
        max = viewModel.maxTemp.value!!.toFloat(),
        onOk = { value ->
            val temp = value.toInt()
            viewModel.setTemp(temp)
            close()
        },
        onCancel = {
            close()
        }
    )
}

@Composable
private fun modeSheet(
    viewModel: DeviceViewModel,
    close: () -> Unit
) {
    BottomSheetList(
        title = "Mode",
        list = viewModel.getSupportedModes(),
        onItemClick = { data ->
            viewModel.setMode(data)
            close()
        }
    )
}

@Composable
private fun sheetContent(
    viewModel: DeviceViewModel,
    currentSheet: DeviceControlSheet,
    close: () -> Unit
) {
    when (currentSheet) {
        DeviceControlSheet.FanSpeed -> fanSpeedSheet(viewModel, close)
        DeviceControlSheet.TempControl -> tempControlSheet(viewModel, close)
        DeviceControlSheet.Mode -> modeSheet(viewModel, close)
        else -> FullscreenLoading()
    }
}

@ExperimentalMaterialApi
@Composable
private fun onScreen(
    viewModel: DeviceViewModel,
    showSheet: (data: DeviceControlSheet) -> Unit
) {
    ScrollableColumn {
        buildTempControl(viewModel, showSheet)
        buildAmbientTemp(viewModel)
        Divider()

        buildMode(viewModel, showSheet)
        buildFanSpeed(viewModel, showSheet)
        buildSleepMode(viewModel)
        buildPower(viewModel)

        buildAirFlow(viewModel)

        PreferenceCategory("Advanced")
        buildBacklight(viewModel)
        buildEco(viewModel)
        buildQuiet(viewModel)
        buildBoost(viewModel)
    }
}

@Composable
private fun buildTempControl(viewModel: DeviceViewModel, onClick: (value: DeviceControlSheet) -> Unit) {
    val temp = viewModel.temp.observeAsState().value
    if (temp != null)
        TemperatureControlStep(
            temp = temp,
            onTempDown = { viewModel.reduceTemp() },
            onTempUp = { viewModel.increaseTemp() },
            onTempClick = {  onClick(DeviceControlSheet.TempControl) }
        )
}

@Composable
private fun buildAmbientTemp(viewModel: DeviceViewModel) {
    val temp = viewModel.roomTemp.observeAsState()

    PreferenceDescription(
        text = stringResource(R.string.device_ambient_temp, temp.value ?: 0),
        icon = vectorResource(id = R.drawable.ic_thermometer),
    )
}

@Composable
private fun buildMode(viewModel: DeviceViewModel, onClick: (value: DeviceControlSheet) -> Unit) {
    val mode = viewModel.workState.observeAsState().value
    val resId = modeToStringMap[mode] ?: R.string.work_mode_unknown
    val drawableId = modeToIconMap[mode] ?: R.drawable.round_brightness_7_24

    Preference(
        title = "Mode",
        summary = stringResource(resId),
        icon = vectorResource(drawableId),
        onClick = { onClick(DeviceControlSheet.Mode) }
    )
}

@Composable
private fun buildFanSpeed(viewModel: DeviceViewModel, onClick: (value: DeviceControlSheet) -> Unit) {
    val fanSpeed = viewModel.fanSpeed.observeAsState().value
    if (fanSpeed != null) {
        val resId = fanToStringMap.getValue(fanSpeed)
        Preference(
            title = "Fan speed",
            summary = stringResource(resId),
            icon = vectorResource(R.drawable.ic_fan),
            onClick = { onClick(DeviceControlSheet.FanSpeed) }
        )
    }
}

@Composable
private fun buildSleepMode(viewModel: DeviceViewModel) {
    val mode = viewModel.sleepMode.observeAsState().value
    if (mode != null) {
        val resId = sleepToStringMap.getValue(mode)
        Preference(
            title = "Sleep mode",
            summary = stringResource(resId),
            icon = vectorResource(R.drawable.ic_nights_stay)
        )
    }
}

@Composable
private fun buildPower(viewModel: DeviceViewModel) {
    SwitchPreference(
        title = "Power",
        summary = "Turn off the device",
        checked = true,
        icon = vectorResource(R.drawable.round_power_settings_new_24),
        onCheckedChange = { viewModel.switchPower() }
    )
}

@Composable
private fun buildAirFlow(viewModel: DeviceViewModel) {
    val horizontal = viewModel.horizontalAirFlow.observeAsState().value
    val vertical = viewModel.verticalAirFlow.observeAsState().value

    if (horizontal != null || vertical != null)
        PreferenceCategory("Air Flow")

    if (horizontal != null)
        SwitchPreference(
            title = "Horizontal",
            checked = horizontal,
            onCheckedChange = { viewModel.switchAirFlowHorizontal() },
            icon = vectorResource(R.drawable.round_swap_horiz_24)
        )

    if (vertical != null)
        SwitchPreference(
            title = "Vertical",
            checked = vertical,
            onCheckedChange = { viewModel.switchAirFlowVertical() },
            icon = vectorResource(R.drawable.round_swap_vert_24)
        )
}

@Composable
private fun buildBacklight(viewModel: DeviceViewModel) {
    val backlight = viewModel.backlight.observeAsState().value
    if (backlight != null)
        SwitchPreference(
            title = "Dimmer",
            checked = backlight,
            onCheckedChange = { viewModel.switchBacklight() },
            icon = vectorResource(R.drawable.ic_lightbulb_md)
        )
}

@Composable
private fun buildEco(viewModel: DeviceViewModel) {
    val eco = viewModel.isEco.observeAsState().value
    if (eco != null)
        SwitchPreference(
            title = "Eco",
            checked = eco,
            onCheckedChange = { viewModel.switchEco() },
            icon = vectorResource(R.drawable.ic_eco)
        )
}

@Composable
private fun buildQuiet(viewModel: DeviceViewModel) {
    val quiet = viewModel.isQuiet.observeAsState().value
    if (quiet != null)
        SwitchPreference(
            title = "Quiet",
            checked = quiet,
            onCheckedChange = { viewModel.switchQuiet() },
            icon = vectorResource(R.drawable.outline_hearing_disabled_24)
        )
}

@Composable
private fun buildBoost(viewModel: DeviceViewModel) {
    val boost = viewModel.isBoost.observeAsState().value
    if (boost != null)
        SwitchPreference(
            title = "Super",
            checked = boost,
            onCheckedChange = { viewModel.switchBoost() },
            icon = vectorResource(R.drawable.ic_fan_plus)
        )
}