package org.alberto97.aircontroller.ui.devicecontrol

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alberto97.aircontroller.ui.common.FullscreenLoading

enum class DeviceControlSheet {
    None,
    FanSpeed,
    Mode,
    TempControl,
    Sleep
}

@ExperimentalMaterialApi
@Composable
fun DeviceControlBottomSheet(
    sheetFanSpeed: @Composable (closeSheet: () -> Unit) -> Unit,
    sheetTempControl: @Composable (closeSheet: () -> Unit) -> Unit,
    sheetMode: @Composable (closeSheet: () -> Unit) -> Unit,
    sheetSleep: @Composable (closeSheet: () -> Unit) -> Unit,
    content: @Composable (openSheet: (sheet: DeviceControlSheet) -> Unit) -> Unit,
) {
    val (sheetType, setSheetType) = rememberSaveable { mutableStateOf(DeviceControlSheet.None) }
    val sheetScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val openSheet = { sheet: DeviceControlSheet ->
        setSheetType(sheet)
        sheetScope.launch {
            delay(100L)
            sheetState.show()
        }
    }

    val closeSheet = {
        sheetScope.launch {
            sheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            when (sheetType) {
                DeviceControlSheet.FanSpeed -> sheetFanSpeed(closeSheet)
                DeviceControlSheet.TempControl -> sheetTempControl(closeSheet)
                DeviceControlSheet.Mode -> sheetMode(closeSheet)
                DeviceControlSheet.Sleep -> sheetSleep(closeSheet)
                else -> FullscreenLoading()
            }
        },
        content = { content(openSheet) }
    )
}