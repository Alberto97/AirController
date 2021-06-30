package org.alberto97.hisenseair.ui.devices

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.Routes
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.ui.FullscreenLoading
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.MainViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@Composable
fun DevicesScreen(
    navController: NavController,
    viewModel: MainViewModel = getViewModel()
) {
    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar({ Text(stringResource(R.string.app_name)) })
            },
        ) {
            val loggedOut by viewModel.isLoggedOut.observeAsState(false)
            if (loggedOut)
                navController.navigate(Routes.Login)

            val isLoading by viewModel.isLoading.observeAsState(true)
            if (isLoading) {
                FullscreenLoading()
            } else {
                val devices by viewModel.devices.observeAsState(listOf())
                Devices(devices, onDeviceClick = { dsn ->
                    navController.navigate("${Routes.DeviceControl}/$dsn")
                })
            }
        }
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
        Scaffold {
            Devices(devices = devices, onDeviceClick = {})
        }
    }
}