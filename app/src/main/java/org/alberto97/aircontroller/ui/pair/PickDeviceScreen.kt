package org.alberto97.aircontroller.ui.pair

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.ui.theme.AppTheme

@Composable
fun PickDeviceScreen(
    viewModel: PairViewModel,
    navigateNetwork: () -> Unit,
    exit: () -> Unit
) {
    val message by viewModel.message.collectAsState()
    val step by viewModel.navAction.collectAsState(PairViewModel.NavAction.PickDevice)

    LaunchedEffect(step) {
        when (step) {
            PairViewModel.NavAction.SelectNetwork  -> navigateNetwork()
            PairViewModel.NavAction.Exit -> exit()
            else -> {}
        }
    }

    PickDeviceScreen(
        message = message,
        onClearMessage = { viewModel.clearMessage() },
        onClick = { viewModel.selectDeviceAp() }
    )
}

@Composable
private fun PickDeviceScreen(
    message: String,
    onClearMessage: () -> Unit,
    onClick: () -> Unit
) {
    PairScaffold(
        title = stringResource(R.string.pair_pick_device_title),
        subtitle = stringResource(R.string.pair_pick_device_subtitle),
        message = message,
        onClearMessage = onClearMessage
    ) {
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
                Text(stringResource(R.string.next))
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            PickDeviceScreen("", {}, {})
        }
    }
}