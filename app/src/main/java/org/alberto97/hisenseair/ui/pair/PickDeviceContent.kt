package org.alberto97.hisenseair.ui.pair

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.PairViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PickDeviceContent(viewModel: PairViewModel) {
    PickDeviceContent(onClick = { viewModel.selectDeviceAp() })
}

@Composable
private fun PickDeviceContent(onClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

            Text(
                "Select the device you wish to connect",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Text("Enable pairing on your device by pressing 6 times on the remote control and wait until the display shows \"77\"",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("")

            Icon(
                painterResource(id = R.drawable.ic_weather_windy),
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
                Text("Next")
            }
        }

    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            PickDeviceContent {}
        }
    }
}