package org.alberto97.hisenseair.ui.devicecontrol

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.ui.BottomSheetList
import org.alberto97.hisenseair.ui.FullscreenLoading
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

enum class DeviceControlSheet {
    None,
    FanSpeed,
    Mode,
    TempControl,
    Sleep
}

@ExperimentalMaterialApi
@Composable
fun DeviceControlScreen(
    dsn: String,
    displayInPanel: Boolean,
    onSettingsClick: () -> Unit,
    viewModel: DeviceViewModel = getViewModel { parametersOf(dsn) }
) {
    val deviceName by viewModel.deviceName.observeAsState("")

    val isLoading by viewModel.isLoading.observeAsState(true)
    val isOn by viewModel.isOn.observeAsState(null)


    val (sheetType, setSheetType) = remember { mutableStateOf(DeviceControlSheet.None) }
    val sheetScope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            SheetContent(
                viewModel = viewModel,
                currentSheet = sheetType,
                close = {
                    sheetScope.launch {
                        state.hide()
                    }
                }
            )
        }
    ){
        Scaffold(
            topBar = {
                TopAppBar(
                    deviceName = deviceName,
                    displayInPanel = displayInPanel,
                    navigateToSettings = onSettingsClick
                )
            }
        ) {
            if (isLoading) {
                FullscreenLoading()
            } else {
                when (isOn) {
                    false -> DeviceOff(viewModel)
                    true -> DeviceOn(
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

@Composable
private fun TopAppBar(
    deviceName: String,
    displayInPanel: Boolean,
    navigateToSettings: () -> Unit
) {

    if (!displayInPanel) {
        TopAppBar(
            title = { Text(deviceName) },
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
}

@Composable
private fun TempControlSheet(
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

@ExperimentalMaterialApi
@Composable
private fun ModeSheet(
    viewModel: DeviceViewModel,
    close: () -> Unit
) {
    val supportedModes by viewModel.supportedModes.observeAsState(emptyList())

    BottomSheetList(
        title = "Mode",
        list = supportedModes,
        onItemClick = { data ->
            viewModel.setMode(data)
            close()
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun FanSpeedSheet(
    viewModel: DeviceViewModel,
    close: () -> Unit
) {
    val supportedFanSpeeds by viewModel.supportedFanSpeeds.observeAsState(emptyList())

    BottomSheetList(
        title = "Fan Speed",
        list = supportedFanSpeeds,
        onItemClick = { data ->
            viewModel.setFanSpeed(data)
            close()
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun SleepSheet(
    viewModel: DeviceViewModel,
    close: () -> Unit
) {
    val supportedSleepModes by viewModel.supportedSleepModes.observeAsState(emptyList())

    BottomSheetList(
        title = "Sleep Mode",
        list = supportedSleepModes,
        onItemClick = { data ->
            viewModel.setSleepMode(data)
            close()
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun SheetContent(
    viewModel: DeviceViewModel,
    currentSheet: DeviceControlSheet,
    close: () -> Unit
) {
    when (currentSheet) {
        DeviceControlSheet.FanSpeed -> FanSpeedSheet(viewModel, close)
        DeviceControlSheet.TempControl -> TempControlSheet(viewModel, close)
        DeviceControlSheet.Mode -> ModeSheet(viewModel, close)
        DeviceControlSheet.Sleep -> SleepSheet(viewModel, close)
        else -> FullscreenLoading()
    }
}
