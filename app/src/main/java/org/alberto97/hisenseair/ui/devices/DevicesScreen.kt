package org.alberto97.hisenseair.ui.devices

import androidx.compose.foundation.Text
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.ui.FullscreenLoading
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.MainViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun DevicesScreen(
    viewModel: MainViewModel = getViewModel(),
    onUnauthorized: () -> Unit,
    onDeviceClick: (id: String) -> Unit
) {
    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar({ Text(stringResource(R.string.app_name)) })
            },
        ) {
            val loggedOut by viewModel.isLoggedOut.observeAsState(false)
            if (loggedOut)
                onUnauthorized()

            val isLoading by viewModel.isLoading.observeAsState(true)
            if (isLoading) {
                FullscreenLoading()
            } else {
                val devices by viewModel.devices.observeAsState(listOf())
                devices(devices, onDeviceClick)
            }
        }
    }
}

@Composable
private fun devices(devices: List<AppDevice>, onDeviceClick: (id: String) -> Unit) {
    LazyColumnFor(devices) { device ->
        DeviceItem(
            id = device.id,
            name = device.name,
            state = if (device.available) "Available" else "Offline",
            onClick = { onDeviceClick(it) }
        )
    }
}

@Preview
@Composable
private fun preview() {
    val devices = listOf(
        AppDevice("1", "test1", true, "", "", ""),
        AppDevice("2", "test2", true, "", "", ""),
    )
    AppTheme {
        Scaffold {
            devices(devices = devices, onDeviceClick = {})
        }
    }
}