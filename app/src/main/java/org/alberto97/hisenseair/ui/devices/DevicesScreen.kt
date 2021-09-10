package org.alberto97.hisenseair.ui.devices

import android.os.Build
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.ui.common.AppScaffold
import org.alberto97.hisenseair.ui.common.FullscreenLoading
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.MainViewModel
import org.koin.androidx.compose.getViewModel

object DevicesStateHandleParams {
    const val needsRefresh = "needs_refresh"
}

@ExperimentalMaterialApi
@Composable
fun DevicesScreen(
    openDevice: (dsn: String) -> Unit,
    openPair: () -> Unit,
    currentBackStackEntry: NavBackStackEntry?,
    viewModel: MainViewModel = getViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val devices by viewModel.devices.collectAsState()
    val message by viewModel.message.collectAsState()

    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    val refreshLiveData = savedStateHandle?.getLiveData<Boolean>(DevicesStateHandleParams.needsRefresh)

    val needsRefresh by refreshLiveData!!.observeAsState()
    if (needsRefresh == true) {
        viewModel.loadData()
        savedStateHandle?.set(DevicesStateHandleParams.needsRefresh, false)
    }

    DevicesScreen(
        message = message,
        clearMessage = { viewModel.clearMessage() },
        isLoading = isLoading,
        deviceList = devices,
        onDeviceClick = openDevice,
        onAddClick = openPair
    )
}

@ExperimentalMaterialApi
@Composable
private fun DevicesScreen(
    message: String,
    clearMessage: () -> Unit,
    isLoading: Boolean,
    deviceList: List<AppDevice>,
    onDeviceClick: (dsn: String) -> Unit,
    onAddClick: () -> Unit
) {
    AppScaffold(
        message = message,
        clearMessage = clearMessage,
        topBar = {
            TopAppBar({ Text(stringResource(R.string.app_name)) })
        },
        floatingActionButton = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                Fab(onAddClick = onAddClick)
        }
    ) {
        if (isLoading) {
            FullscreenLoading()
        } else {
            Devices(devices = deviceList, onDeviceClick = onDeviceClick)
        }
    }
}

@Composable
private fun Fab(onAddClick: () -> Unit) {
    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.primary,
        onClick = onAddClick
    ) {
        Icon(
            Icons.Outlined.Add,
            tint = Color.White,
            contentDescription = ""
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun Devices(devices: List<AppDevice>, onDeviceClick: (id: String) -> Unit) {
    LazyColumn {
        items(devices) { device ->
            DeviceItem(
                id = device.id,
                name = device.name,
                state = if (device.available) "Available" else "Offline",
                onClick = { onDeviceClick(it) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun Preview() {
    val devices = listOf(
        AppDevice("1", "test1", true, "", "", ""),
        AppDevice("2", "test2", true, "", "", ""),
    )

    AppTheme {
        DevicesScreen(
            message = "",
            clearMessage = {},
            isLoading = false,
            deviceList = devices,
            onDeviceClick = {},
            onAddClick = {}
        )
    }
}