package org.alberto97.hisenseair.ui.devicesettings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.devices.DevicesStateHandleParams
import org.alberto97.hisenseair.ui.preferences.DialogPreference
import org.alberto97.hisenseair.ui.preferences.Preference
import org.alberto97.hisenseair.ui.preferences.PreferenceCategory
import org.alberto97.hisenseair.ui.preferences.SwitchPreference
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.DevicePreferenceViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@ExperimentalMaterialApi
@Composable
fun DeviceSettingsScreen(
    dsn: String,
    navigateUp: () -> Unit,
    navigateHome: () -> Unit,
    homeBackStackEntry: NavBackStackEntry,
    viewModel: DevicePreferenceViewModel = getViewModel { parametersOf(dsn) }
) {
    val deviceName by viewModel.deviceName.observeAsState("")
    val popToHome by viewModel.popToHome.observeAsState(false)
    val usesCelsius by viewModel.useCelsius.observeAsState(true)
    val macAddress by viewModel.mac.observeAsState("")
    val ipAddress by viewModel.ip.observeAsState("")
    val network by viewModel.ssid.observeAsState("")

    LaunchedEffect(popToHome) {
        if (popToHome) {
            val savedStateHandle = homeBackStackEntry.savedStateHandle
            savedStateHandle.set(DevicesStateHandleParams.needsRefresh, true)
            navigateHome()
        }
    }

    DeviceSettingsScreen(
        deviceName = deviceName,
        renameDevice = { name -> viewModel.updateDeviceName(name) },
        useCelsius = usesCelsius,
        onUseCelsiusChange = { viewModel.switchTempType() },
        deleteDevice = { viewModel.deleteDevice() },
        macAddress = macAddress,
        ipAddress = ipAddress,
        network = network,
        navigateUp = navigateUp
    )
}
@ExperimentalMaterialApi
@Composable
private fun DeviceSettingsScreen(
    deviceName: String,
    renameDevice: (value: String) -> Unit,
    useCelsius: Boolean,
    onUseCelsiusChange: (value: Boolean) -> Unit,
    deleteDevice: () -> Unit,
    macAddress: String,
    ipAddress: String,
    network: String,
    navigateUp: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.device_settings)) },
            navigationIcon = {
                IconButton(onClick = { navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
        )
    }) {
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
            renameDevice = {},
            useCelsius = true,
            onUseCelsiusChange = {},
            deleteDevice = {},
            macAddress = "FF:FF:FF:FF:FF:FF",
            ipAddress = "192.168.0.56",
            network = "HomeWifi",
            navigateUp = {}
        )
    }
}