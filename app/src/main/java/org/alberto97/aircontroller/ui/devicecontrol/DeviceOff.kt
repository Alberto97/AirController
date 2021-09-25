package org.alberto97.aircontroller.ui.devicecontrol

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.common.features.WorkMode
import org.alberto97.aircontroller.features.modeToIconMap
import org.alberto97.aircontroller.ui.theme.AppTheme
import org.alberto97.aircontroller.viewmodels.DeviceViewModel

@Composable
fun DeviceOff(viewModel: DeviceViewModel) {
    val currentTemp by viewModel.roomTemp.collectAsState()
    val currentMode by viewModel.workMode.collectAsState()

    DeviceOff(
        currentMode = currentMode,
        currentTemp = currentTemp,
        onPower = { viewModel.switchPower() }
    )
}

@Composable
private fun DeviceOff(currentMode: WorkMode, currentTemp: Int, onPower: () -> Unit) {
    val drawableId = modeToIconMap[currentMode] ?: R.drawable.outline_brightness_low

    Row(
        Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(drawableId),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                alpha = 0.5f,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .width(56.dp)
                    .height(56.dp)
            )
            Text(
                text = stringResource(R.string.device_off),
                style = MaterialTheme.typography.h6
            )
            Text(
                text = stringResource(R.string.device_room_temp, currentTemp),
                style = MaterialTheme.typography.subtitle1
            )
            FloatingActionButton(
                onClick = onPower,
                modifier = Modifier.padding(all = 16.dp),
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Icon(
                    Icons.Rounded.PowerSettingsNew,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            DeviceOff(WorkMode.FanOnly, 16) {}
        }
    }
}