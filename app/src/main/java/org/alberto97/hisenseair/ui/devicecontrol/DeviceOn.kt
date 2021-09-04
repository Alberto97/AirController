package org.alberto97.hisenseair.ui.devicecontrol

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HearingDisabled
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material.icons.rounded.SwapVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.features.*
import org.alberto97.hisenseair.ui.preferences.Preference
import org.alberto97.hisenseair.ui.preferences.PreferenceCategory
import org.alberto97.hisenseair.ui.preferences.PreferenceDescription
import org.alberto97.hisenseair.ui.preferences.SwitchPreference
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.DeviceViewModel

@Composable
@ExperimentalMaterialApi
fun DeviceOn(viewModel: DeviceViewModel, showSheet: (data: DeviceControlSheet) -> Unit) {
    val temp by viewModel.temp.observeAsState()
    val roomTemp by viewModel.roomTemp.observeAsState()
    val workMode by viewModel.workState.observeAsState()
    val fanSpeed by viewModel.fanSpeed.observeAsState()
    val sleepMode by viewModel.sleepMode.observeAsState()
    val horizontalAirFlow by viewModel.horizontalAirFlow.observeAsState()
    val verticalAirFlow by viewModel.verticalAirFlow.observeAsState()
    val backlight by viewModel.backlight.observeAsState()
    val eco by viewModel.isEco.observeAsState()
    val quiet by viewModel.isQuiet.observeAsState()
    val boost by viewModel.isBoost.observeAsState()

    DeviceOn(
        temp = temp,
        incTemp = { viewModel.increaseTemp() },
        decTemp = { viewModel.reduceTemp() },
        roomTemp = roomTemp,
        workMode = workMode,
        fanSpeed = fanSpeed,
        sleepMode = sleepMode,
        switchPower = { viewModel.switchPower() },
        horizontalAirFlow = horizontalAirFlow,
        switchHorizontal = { viewModel.switchAirFlowHorizontal() },
        verticalAirFlow = verticalAirFlow,
        switchVertical = { viewModel.switchAirFlowVertical() },
        backlight = backlight,
        switchBacklight = { viewModel.switchBacklight() },
        eco = eco,
        switchEco = { viewModel.switchEco() },
        quiet = quiet,
        switchQuiet = { viewModel.switchQuiet() },
        boost = boost,
        switchBoost = { viewModel.switchBoost() },
        showSheet = showSheet
    )
}

@Composable
@ExperimentalMaterialApi
private fun DeviceOn(
    temp: Int?,
    incTemp: () -> Unit,
    decTemp: () -> Unit,
    roomTemp: Int?,
    workMode: WorkMode?,
    fanSpeed: FanSpeed?,
    sleepMode: SleepMode?,
    switchPower: () -> Unit,
    horizontalAirFlow: Boolean?,
    switchHorizontal: () -> Unit,
    verticalAirFlow: Boolean?,
    switchVertical: () -> Unit,
    backlight: Boolean?,
    switchBacklight: () -> Unit,
    eco: Boolean?,
    switchEco: () -> Unit,
    quiet: Boolean?,
    switchQuiet: () -> Unit,
    boost: Boolean?,
    switchBoost: () -> Unit,
    showSheet: (data: DeviceControlSheet) -> Unit
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        TempControl(
            temp = temp,
            incTemp = incTemp,
            decTemp = decTemp,
            onClick = showSheet
        )
        AmbientTemp(roomTemp)

        Divider()

        Mode(workMode, showSheet)
        FanSpeed(fanSpeed, showSheet)
        SleepMode(sleepMode, showSheet)
        Power(switchPower)

        AirFlow(
            horizontal = horizontalAirFlow,
            switchHorizontal = switchHorizontal,
            vertical = verticalAirFlow,
            switchVertical = switchVertical
        )

        PreferenceCategory("Advanced")
        Backlight(backlight, switchBacklight)
        Eco(eco, switchEco)
        Quiet(quiet, switchQuiet)
        Boost(boost, switchBoost)
    }
}


@Composable
private fun TempControl(
    temp: Int?,
    incTemp: () -> Unit,
    decTemp: () -> Unit,
    onClick: (value: DeviceControlSheet) -> Unit
) {
    if (temp != null)
        TemperatureControlStep(
            temp = temp,
            onTempDown = decTemp,
            onTempUp = incTemp,
            onTempClick = {  onClick(DeviceControlSheet.TempControl) }
        )
}

@Composable
private fun AmbientTemp(roomTemp: Int?) {
    PreferenceDescription(
        text = stringResource(R.string.device_ambient_temp, roomTemp ?: 0),
        icon = rememberVectorPainter(Icons.Outlined.Thermostat),
    )
}

@ExperimentalMaterialApi
@Composable
private fun Mode(workMode: WorkMode?, onClick: (value: DeviceControlSheet) -> Unit) {
    val resId = modeToStringMap.getOrDefault(workMode, R.string.work_mode_unknown)
    val drawableId = modeToIconMap.getOrDefault(workMode, R.drawable.outline_brightness_low)

    Preference(
        title = "Mode",
        summary = stringResource(resId),
        icon = painterResource(drawableId),
        onClick = { onClick(DeviceControlSheet.Mode) }
    )
}

@ExperimentalMaterialApi
@Composable
private fun FanSpeed(fanSpeed: FanSpeed?, onClick: (value: DeviceControlSheet) -> Unit) {
    val resId = fanToStringMap.getOrDefault(fanSpeed, R.string.fan_speed_auto)
    if (fanSpeed != null) {
        Preference(
            title = "Fan speed",
            summary = stringResource(resId),
            icon = painterResource(R.drawable.ic_fan),
            onClick = { onClick(DeviceControlSheet.FanSpeed) }
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun SleepMode(
    sleepMode: SleepMode?,
    onClick: (value: DeviceControlSheet) -> Unit
) {
    if (sleepMode != null) {
        val resId = sleepToStringMap.getValue(sleepMode)
        Preference(
            title = "Sleep mode",
            summary = stringResource(resId),
            icon = painterResource(R.drawable.ic_nights_stay),
            onClick = { onClick(DeviceControlSheet.Sleep) }
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun Power(onOffClick: () -> Unit) {
    SwitchPreference(
        title = "Power",
        summary = "Turn off the device",
        checked = true,
        icon = rememberVectorPainter(Icons.Rounded.PowerSettingsNew),
        onCheckedChange = { onOffClick() }
    )
}

@ExperimentalMaterialApi
@Composable
private fun AirFlow(
    horizontal: Boolean?,
    switchHorizontal: () -> Unit,
    vertical: Boolean?,
    switchVertical: () -> Unit
) {
    if (horizontal != null || vertical != null)
        PreferenceCategory("Air Flow")

    if (horizontal != null)
        SwitchPreference(
            title = "Horizontal",
            checked = horizontal,
            onCheckedChange = { switchHorizontal() },
            icon = rememberVectorPainter(Icons.Rounded.SwapHoriz)
        )

    if (vertical != null)
        SwitchPreference(
            title = "Vertical",
            checked = vertical,
            onCheckedChange = { switchVertical() },
            icon = rememberVectorPainter(Icons.Rounded.SwapVert)
        )
}

@ExperimentalMaterialApi
@Composable
private fun Backlight(backlight: Boolean?, switchBacklight: () -> Unit) {
    if (backlight != null)
        SwitchPreference(
            title = "Dimmer",
            checked = backlight,
            onCheckedChange = { switchBacklight() },
            icon = rememberVectorPainter(Icons.Outlined.Lightbulb)
        )
}

@ExperimentalMaterialApi
@Composable
private fun Eco(eco: Boolean?, switchEco: () -> Unit) {
    if (eco != null)
        SwitchPreference(
            title = "Eco",
            checked = eco,
            onCheckedChange = { switchEco() },
            icon = painterResource(R.drawable.ic_eco)
        )
}

@ExperimentalMaterialApi
@Composable
private fun Quiet(quiet: Boolean?, switchQuiet: () -> Unit) {
    if (quiet != null)
        SwitchPreference(
            title = "Quiet",
            checked = quiet,
            onCheckedChange = { switchQuiet() },
            icon = rememberVectorPainter(Icons.Outlined.HearingDisabled)
        )
}

@ExperimentalMaterialApi
@Composable
private fun Boost(boost: Boolean?, switchBoost: () -> Unit) {
    if (boost != null)
        SwitchPreference(
            title = "Super",
            checked = boost,
            onCheckedChange = { switchBoost() },
            icon = painterResource(R.drawable.ic_fan_plus)
        )
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            DeviceOn(
                temp = 0,
                incTemp = { },
                decTemp = { },
                roomTemp = 0,
                workMode = WorkMode.Auto,
                fanSpeed = FanSpeed.Auto,
                sleepMode = SleepMode.OFF,
                switchPower = {  },
                horizontalAirFlow = false,
                switchHorizontal = { },
                verticalAirFlow = false,
                switchVertical = { },
                backlight = false,
                switchBacklight = { },
                eco = true,
                switchEco = { },
                quiet = true,
                switchQuiet = { },
                boost = false,
                switchBoost = { },
                showSheet = { }
            )
        }
    }
}
