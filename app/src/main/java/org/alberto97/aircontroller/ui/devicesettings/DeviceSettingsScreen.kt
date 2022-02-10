package org.alberto97.aircontroller.ui.devicesettings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.models.ScreenState
import org.alberto97.aircontroller.ui.common.AppScaffold
import org.alberto97.aircontroller.ui.common.AppToolbar
import org.alberto97.aircontroller.ui.common.FullscreenError
import org.alberto97.aircontroller.ui.common.FullscreenLoading
import org.alberto97.aircontroller.ui.devices.DevicesStateHandleParams
import org.alberto97.aircontroller.ui.preferences.DialogPreference
import org.alberto97.aircontroller.ui.preferences.Preference
import org.alberto97.aircontroller.ui.preferences.PreferenceCategory
import org.alberto97.aircontroller.ui.preferences.SwitchPreference
import org.alberto97.aircontroller.ui.theme.AppTheme
import org.alberto97.aircontroller.viewmodels.DevicePreferenceViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeviceSettingsScreen(
    dsn: String,
    navigateUp: () -> Unit,
    navigateHome: () -> Unit,
    homeBackStackEntry: NavBackStackEntry,
    viewModel: DevicePreferenceViewModel = getViewModel { parametersOf(dsn) }
) {
    val deviceName by viewModel.deviceName.collectAsState()
    val state by viewModel.state.collectAsState()
    val popToHome by viewModel.popToHome.collectAsState()
    val usesCelsius by viewModel.useCelsius.collectAsState()
    val macAddress by viewModel.mac.collectAsState()
    val ipAddress by viewModel.ip.collectAsState()
    val network by viewModel.ssid.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(popToHome) {
        if (popToHome) {
            val savedStateHandle = homeBackStackEntry.savedStateHandle
            savedStateHandle.set(DevicesStateHandleParams.needsRefresh, true)
            navigateHome()
        }
    }

    DeviceSettingsScreen(
        deviceName = deviceName,
        state = state,
        loadData = { viewModel.loadData() },
        renameDevice = { name -> viewModel.updateDeviceName(name) },
        useCelsius = usesCelsius,
        onUseCelsiusChange = { viewModel.switchTempType() },
        deleteDevice = { viewModel.deleteDevice() },
        macAddress = macAddress,
        ipAddress = ipAddress,
        network = network,
        message = message,
        clearMessage = { viewModel.clearMessage() },
        navigateUp = navigateUp
    )
}
@ExperimentalMaterialApi
@Composable
private fun DeviceSettingsScreen(
    deviceName: String,
    state: ScreenState,
    loadData: () -> Unit,
    renameDevice: (value: String) -> Unit,
    useCelsius: Boolean,
    onUseCelsiusChange: (value: Boolean) -> Unit,
    deleteDevice: () -> Unit,
    macAddress: String,
    ipAddress: String,
    network: String,
    message: String,
    clearMessage: () -> Unit,
    navigateUp: () -> Unit
) {
    AppScaffold(
        message = message,
        clearMessage = clearMessage,
        topBar = {
            AppToolbar(
                title = { Text(stringResource(R.string.device_settings)) },
                navigateUp = navigateUp
            )
        }
    ) {
        when(state) {
            ScreenState.Loading -> FullscreenLoading()
            ScreenState.Error -> FullscreenError(loadData)
            else -> DeviceSettingsContent(
                deviceName = deviceName,
                renameDevice = renameDevice,
                useCelsius = useCelsius,
                onUseCelsiusChange = onUseCelsiusChange,
                deleteDevice = deleteDevice,
                macAddress = macAddress,
                ipAddress = ipAddress,
                network = network
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun DeviceSettingsContent(
    deviceName: String,
    renameDevice: (value: String) -> Unit,
    useCelsius: Boolean,
    onUseCelsiusChange: (value: Boolean) -> Unit,
    deleteDevice: () -> Unit,
    macAddress: String,
    ipAddress: String,
    network: String
) {
    Column {
        DialogPreference(
            icon = {},
            title = stringResource(R.string.device_name_title),
            summary = deviceName,
            dialog = { closeDialog ->
                ChangeNameAlert(
                    closeDialog = closeDialog,
                    initialDeviceName = deviceName,
                    setName = renameDevice
                )
            }
        )
        SwitchPreference(
            icon = {},
            title = stringResource(R.string.device_temp_unit_title),
            summary = stringResource(R.string.device_temp_unit_summary),
            checked = useCelsius,
            onCheckedChange = onUseCelsiusChange
        )
        DialogPreference(
            icon = {},
            title = stringResource(R.string.device_delete_title),
            summary = stringResource(R.string.device_delete_summary),
            dialog = { closeDialog -> DeleteAlert(closeDialog, deleteDevice) }
        )
        PreferenceCategory(
            title = stringResource(R.string.device_settings_category_info)
        )
        Preference(
            icon = {},
            title = stringResource(R.string.device_mac_address_title),
            summary = macAddress
        )
        Preference(
            icon = {},
            title = stringResource(R.string.device_ip_address_title),
            summary = ipAddress
        )
        Preference(
            icon = {},
            title = stringResource(R.string.device_network_title),
            summary = network
        )
    }
}

@Composable
private fun ChangeNameAlert(
    closeDialog: () -> Unit,
    initialDeviceName: String,
    setName: (value: String) -> Unit
) {
    val (deviceName, setDeviceName) = remember { mutableStateOf(initialDeviceName) }

    AlertDialog(
        onDismissRequest = closeDialog,
        title = {
            Text(stringResource(R.string.device_name_alert_title))
        },
        text = {
            Text("")
            OutlinedTextField(value = deviceName, onValueChange = setDeviceName)
        },
        confirmButton = {
            TextButton(
                content = { Text(stringResource(android.R.string.ok)) },
                onClick = {
                    closeDialog()
                    setName(deviceName)
                }
            )
        },
        dismissButton = {
            TextButton(
                content = { Text(stringResource(android.R.string.cancel)) },
                onClick = closeDialog
            )
        }
    )
}

@Composable
private fun DeleteAlert(
    closeDialog: () -> Unit,
    deleteDevice: () -> Unit
) {
    AlertDialog(
        onDismissRequest = closeDialog,
        title = {
            Text(stringResource(R.string.device_delete_alert_title))
        },
        text = {
            Text(stringResource(R.string.device_delete_alert_message))
        },
        confirmButton = {
            TextButton(
                content = { Text(stringResource(android.R.string.ok)) },
                onClick = {
                    closeDialog()
                    deleteDevice()
                }
            )
        },
        dismissButton = {
            TextButton(
                content = { Text(stringResource(android.R.string.cancel)) },
                onClick = closeDialog
            )
        }
    )
}

@Composable
@ExperimentalMaterialApi
@Preview
private fun Preview() {
    AppTheme {
        DeviceSettingsScreen(
            deviceName = "Camera piano terra",
            loadData = {},
            state = ScreenState.Success,
            renameDevice = {},
            useCelsius = true,
            onUseCelsiusChange = {},
            deleteDevice = {},
            macAddress = "FF:FF:FF:FF:FF:FF",
            ipAddress = "192.168.0.56",
            network = "HomeWifi",
            message = "",
            clearMessage = {},
            navigateUp = {}
        )
    }
}