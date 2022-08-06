package org.alberto97.aircontroller.ui.devicecontrol

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.features.fanToStringMap
import org.alberto97.aircontroller.features.modeToIconMap
import org.alberto97.aircontroller.features.modeToStringMap
import org.alberto97.aircontroller.features.sleepToStringMap
import org.alberto97.aircontroller.models.ScreenState
import org.alberto97.aircontroller.ui.common.*
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeviceControlScreen(
    dsn: String,
    displayInPanel: Boolean,
    navigateUp: () -> Unit,
    navigateSettings: () -> Unit,
    viewModel: DeviceViewModel = getViewModel { parametersOf(dsn) }
) {
    val deviceName by viewModel.deviceName.collectAsState()

    val state by viewModel.state.collectAsState()
    val isOn by viewModel.isOn.collectAsState()
    val message by viewModel.message.collectAsState()

    DeviceControlScreen(
        viewModel = viewModel,
        message = message,
        clearMessage = { viewModel.clearMessage() },
        loadData = { viewModel.loadData() },
        deviceName = deviceName,
        state = state,
        isOn = isOn,
        displayInPanel = displayInPanel,
        navigateSettings = navigateSettings,
        navigateUp = navigateUp
    )
}

@ExperimentalMaterialApi
@Composable
private fun DeviceControlScreen(
    viewModel: DeviceViewModel,
    message: String,
    clearMessage: () -> Unit,
    loadData: () -> Unit,
    deviceName: String,
    state: ScreenState,
    isOn: Boolean?,
    displayInPanel: Boolean,
    navigateSettings: () -> Unit,
    navigateUp: () -> Unit
) {
    DeviceControlBottomSheet(
        sheetFanSpeed = { closeSheet -> FanSpeedSheet(viewModel, closeSheet) },
        sheetTempControl = { closeSheet -> TempControlSheet(viewModel, closeSheet) },
        sheetMode = { closeSheet ->  ModeSheet(viewModel, closeSheet) },
        sheetSleep = { closeSheet ->  SleepSheet(viewModel, closeSheet) }
    ){ openSheet ->
        AppScaffold(
            message = message,
            clearMessage = clearMessage,
            topBar = {
                TopAppBar(
                    deviceName = deviceName,
                    displayInPanel = displayInPanel,
                    navigateUp = navigateUp,
                    navigateToSettings = navigateSettings
                )
            }
        ) { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                when (state) {
                    ScreenState.Loading -> FullscreenLoading()
                    ScreenState.Error -> FullscreenError(loadData)
                    else -> LoadedScreen(viewModel, isOn, openSheet)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun LoadedScreen(
    viewModel: DeviceViewModel,
    isOn: Boolean?,
    showSheet: (data: DeviceControlSheet) -> Unit
) {
    when (isOn) {
        false -> DeviceOff(viewModel)
        true -> DeviceOn(viewModel, showSheet)
        else -> FullscreenLoading()
    }
}

@Composable
private fun TopAppBar(
    deviceName: String,
    displayInPanel: Boolean,
    navigateUp: () -> Unit,
    navigateToSettings: () -> Unit
) {

    if (!displayInPanel)
        AppToolbar(
            title = { Text(deviceName) },
            navigateUp = navigateUp,
            actions = {
                IconButton({ navigateToSettings() }) {
                    Icon(
                        Icons.Rounded.Settings,
                        contentDescription = null
                    )
                }
            }
        )
}

@Composable
private fun TempControlSheet(
    viewModel: DeviceViewModel,
    close: () -> Unit
) {
    val temp by viewModel.temp.collectAsState()
    val minTemp by viewModel.minTemp.collectAsState()
    val maxTemp by viewModel.maxTemp.collectAsState()

    TemperatureControlDialog(
        temp = temp!!.toFloat(),
        min = minTemp!!.toFloat(),
        max = maxTemp!!.toFloat(),
        onOk = { value ->
            val tmp = value.toInt()
            viewModel.setTemp(tmp)
            close()
        },
        onCancel = {
            close()
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun ModeSheet(
    viewModel: DeviceViewModel,
    close: () -> Unit
) {
    val supportedModes by viewModel.supportedModes.collectAsState()
    val currentMode by viewModel.workMode.collectAsState()

    BottomSheetContent(
        title = "Mode",
        content = {
            supportedModes.forEach { mode ->
                val stringId = modeToStringMap.getValue(mode)
                val drawableId = modeToIconMap.getValue(mode)

                BottomSheetListItem(
                    text = stringResource(stringId),
                    icon = { SheetIcon(drawableId) },
                    selected = mode == currentMode,
                    onClick = {
                        viewModel.setMode(mode)
                        close()
                    }
                )

            }
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun FanSpeedSheet(
    viewModel: DeviceViewModel,
    close: () -> Unit
) {
    val supportedFanSpeeds by viewModel.supportedFanSpeeds.collectAsState()
    val currentFanSpeed by viewModel.fanSpeed.collectAsState()

    BottomSheetContent(
        title = "Fan Speed",
        content = {
            supportedFanSpeeds.forEach { fanSpeed ->
                val stringId = fanToStringMap.getValue(fanSpeed)

                BottomSheetListItem(
                    text = stringResource(stringId),
                    icon = { SheetIcon(R.drawable.ic_fan) },
                    selected = fanSpeed == currentFanSpeed,
                    onClick = {
                        viewModel.setFanSpeed(fanSpeed)
                        close()
                    }
                )
            }
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun SleepSheet(
    viewModel: DeviceViewModel,
    close: () -> Unit
) {
    val supportedSleepModes by viewModel.supportedSleepModes.collectAsState(emptyList())
    val currentSleepMode by viewModel.sleepMode.collectAsState()

    BottomSheetContent(
        title = "Sleep Mode",
        content = {
            supportedSleepModes.forEach { mode ->
                val stringId = sleepToStringMap.getValue(mode.type)

                BottomSheetListItem(
                    text = stringResource(stringId),
                    icon = { SheetIcon(R.drawable.ic_nights_stay) },
                    selected = mode.type == currentSleepMode,
                    onClick = {
                        viewModel.setSleepMode(mode.type)
                        close()
                    }
                )
            }
        }
    )
}

@Composable
private fun SheetIcon(icon: Int) {
    Icon(
        painterResource(icon),
        contentDescription = null,
        tint = Color.Gray
    )
}
