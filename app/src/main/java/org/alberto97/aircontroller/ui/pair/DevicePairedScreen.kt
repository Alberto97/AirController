package org.alberto97.aircontroller.ui.pair

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MobileFriendly
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.ui.devices.DevicesStateHandleParams
import org.alberto97.aircontroller.ui.theme.AppTheme

@Composable
fun DevicePairedScreen(
    previousBackStackEntry: NavBackStackEntry?,
    finish: () -> Unit,
    viewModel: PairViewModel
) {
    val deviceName by viewModel.deviceName.collectAsState()

    DevicePairedScreen(
        deviceName = deviceName,
        onClick = {
            val savedStateHandle = previousBackStackEntry?.savedStateHandle
            savedStateHandle?.set(DevicesStateHandleParams.needsRefresh, true)
            finish()
        }
    )
}

@Composable
private fun DevicePairedScreen(deviceName: String, onClick: () -> Unit) {
    PairScaffold(
        title = stringResource(R.string.pair_device_connected_title),
        subtitle = deviceName
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("")

            Icon(
                Icons.Outlined.MobileFriendly,
                contentDescription = null,
                tint = MaterialTheme.colors.primary.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Button(
                onClick = onClick, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(stringResource(R.string.done))
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface{
            DevicePairedScreen("Hi-Smart-serialNumber", {})
        }
    }
}