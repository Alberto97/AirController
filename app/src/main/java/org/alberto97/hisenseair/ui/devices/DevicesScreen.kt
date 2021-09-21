package org.alberto97.hisenseair.ui.devices

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.common.models.AppDevice
import org.alberto97.hisenseair.models.ScreenState
import org.alberto97.hisenseair.ui.common.AppScaffold
import org.alberto97.hisenseair.ui.common.FullscreenError
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
    openLogin: () -> Unit,
    currentBackStackEntry: NavBackStackEntry?,
    viewModel: MainViewModel = getViewModel()
) {
    val state by viewModel.state.collectAsState()
    val devices by viewModel.devices.collectAsState()
    val message by viewModel.message.collectAsState()
    val pairAvailable by viewModel.pairAvailable.collectAsState(false)

    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    val refreshLiveData = savedStateHandle?.getLiveData<Boolean>(DevicesStateHandleParams.needsRefresh)

    val needsRefresh by refreshLiveData!!.observeAsState()
    if (needsRefresh == true) {
        viewModel.refreshData()
        savedStateHandle?.set(DevicesStateHandleParams.needsRefresh, false)
    }

    DevicesScreen(
        message = message,
        clearMessage = { viewModel.clearMessage() },
        state = state,
        loadData = { viewModel.loadData() },
        pairAvailable = pairAvailable,
        deviceList = devices,
        onDeviceClick = openDevice,
        onAddClick = openPair,
        onLogoutClick = {
            viewModel.logOut()
            openLogin()
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun DevicesScreen(
    message: String,
    clearMessage: () -> Unit,
    state: ScreenState,
    loadData: () -> Unit,
    pairAvailable: Boolean,
    deviceList: List<AppDevice>,
    onDeviceClick: (dsn: String) -> Unit,
    onAddClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    AppScaffold(
        message = message,
        clearMessage = clearMessage,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    DropdownButton(onLogoutClick)
                }
            )
        },
        floatingActionButton = {
            if (pairAvailable)
                Fab(onAddClick = onAddClick)
        }
    ) {
        when (state) {
            ScreenState.Loading -> FullscreenLoading()
            ScreenState.Error -> FullscreenError(tryAgain = loadData)
            else -> Devices(devices = deviceList, onDeviceClick = onDeviceClick)
        }
    }
}

@Composable
fun DropdownButton(onInfoClick: () -> Unit) {
    val (dropdownExpanded, setDropdownExpanded) = remember { mutableStateOf(false) }

    IconButton(onClick = { setDropdownExpanded(true) }) {
        Icon(Icons.Filled.MoreVert, contentDescription = "More")
        Dropdown(
            expanded = dropdownExpanded,
            onDismissRequest = { setDropdownExpanded(false) },
            onInfoClick = onInfoClick
        )
    }
}

@Composable
fun Dropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onInfoClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() }
    ) {
        DropdownMenuItem(onClick = { onInfoClick() }) {
            Text("Logout")
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
            state = ScreenState.Success,
            loadData = {},
            pairAvailable = true,
            deviceList = devices,
            onDeviceClick = {},
            onAddClick = {},
            onLogoutClick = {}
        )
    }
}