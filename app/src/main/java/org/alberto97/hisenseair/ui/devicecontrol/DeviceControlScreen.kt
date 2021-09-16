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
import org.alberto97.hisenseair.models.ScreenState
import org.alberto97.hisenseair.ui.common.*
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
    val (sheetType, setSheetType) = remember { mutableStateOf(DeviceControlSheet.None) }
    val sheetScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            SheetContent(
                viewModel = viewModel,
                currentSheet = sheetType,
                close = {
                    sheetScope.launch {
                        sheetState.hide()
                    }
                }
            )
        }
    ){
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
        ) {
            when (state) {
                ScreenState.Loading -> FullscreenLoading()
                ScreenState.Error -> FullscreenError(loadData)
                else -> LoadedScreen(
                    viewModel = viewModel,
                    isOn = isOn,
                    showSheet = {
                        setSheetType(it)
                        sheetScope.launch {
                            delay(100L)
                            sheetState.show()
                        }
                    }
                )
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
