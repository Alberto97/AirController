package org.alberto97.hisenseair.ui.devices

import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.ui.FullscreenLoading
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.MainViewModel

@Composable
fun DevicesScreen(viewModel: MainViewModel, onDeviceClick: (id: String) -> Unit) {
    AppTheme {
        Scaffold {
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