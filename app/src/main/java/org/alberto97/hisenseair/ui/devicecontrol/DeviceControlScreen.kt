package org.alberto97.hisenseair.ui.devicecontrol

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.ui.common.BottomSheetContent
import org.alberto97.hisenseair.ui.common.BottomSheetListItem
import org.alberto97.hisenseair.ui.common.FullscreenLoading
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
    navigateUp: () -> Unit,
    navigateSettings: () -> Unit,
    viewModel: DeviceViewModel = getViewModel { parametersOf(dsn) }
) {
    val deviceName by viewModel.deviceName.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val isOn by viewModel.isOn.collectAsState()


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
                    navigateUp = navigateUp,
                    navigateToSettings = navigateSettings
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
    navigateUp: () -> Unit,
    navigateToSettings: () -> Unit
) {

    if (!displayInPanel) {
        TopAppBar(
            title = { Text(deviceName) },
            navigationIcon = {
                IconButton(
                    onClick = navigateUp,
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
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
    val supportedModes by viewModel.supportedModes.collectAsState(emptyList())

    BottomSheetContent(
        title = "Mode",
        content = {
            supportedModes.forEach { mode ->
                BottomSheetListItem(
                    text = mode.label,
                    icon = { SheetIcon(mode.resourceDrawable) },
                    selected = mode.selected,
                    onClick = {
                        viewModel.setMode(mode.value)
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
    val supportedFanSpeeds by viewModel.supportedFanSpeeds.collectAsState(emptyList())

    BottomSheetContent(
        title = "Fan Speed",
        content = {
            supportedFanSpeeds.forEach { mode ->
                BottomSheetListItem(
                    text = mode.label,
                    icon = { SheetIcon(mode.resourceDrawable) },
                    selected = mode.selected,
                    onClick = {
                        viewModel.setFanSpeed(mode.value)
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

    BottomSheetContent(
        title = "Sleep Mode",
        content = {
            supportedSleepModes.forEach { mode ->
                BottomSheetListItem(
                    text = mode.label,
                    icon = { SheetIcon(mode.resourceDrawable) },
                    selected = mode.selected,
                    onClick = {
                        viewModel.setSleepMode(mode.value)
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
