package org.alberto97.hisenseair.ui.devicecontrol

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun DeviceOff(modeAsset: VectorAsset, currentTemp: Int, onPower: () -> Unit) {
    Row(
        Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                asset = modeAsset,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                alpha = 0.5f,
                modifier = Modifier.padding(all = 16.dp).width(56.dp).height(56.dp)
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
                    asset = vectorResource(R.drawable.round_power_settings_new_24),
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun preview() {
    AppTheme {
        Surface {
            DeviceOff(vectorResource(R.drawable.ic_weather_windy), 16, {})
        }
    }
}