package org.alberto97.hisenseair.ui.pair

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ayla.internal.models.WifiScanResults
import org.alberto97.hisenseair.ui.common.FullscreenLoading
import org.alberto97.hisenseair.ui.preferences.PreferenceIcon
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.PairViewModel

@ExperimentalMaterialApi
@Composable
fun SelectNetworkScreen(
    viewModel: PairViewModel,
    navigatePassword: () -> Unit,
    exit: () -> Unit
) {
    val message by viewModel.message.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val results by viewModel.scanResults.collectAsState()
    val step by viewModel.navAction.collectAsState(PairViewModel.NavAction.SelectNetwork)

    LaunchedEffect(step) {
        when (step) {
            PairViewModel.NavAction.InsertPassword  -> navigatePassword()
            PairViewModel.NavAction.Exit -> exit()
            else -> {}
        }
    }

    SelectNetworkScreen(
        loading = loading,
        message = message,
        onClearMessage = { viewModel.clearMessage() },
        list = results,
        onClick = { viewModel.setSelectedSsid(it) }
    )
}

@ExperimentalMaterialApi
@Composable
private fun SelectNetworkScreen(
    message: String,
    onClearMessage: () -> Unit,
    loading: Boolean,
    list: List<WifiScanResults.WifiScanResult>,
    onClick: (network: WifiScanResults.WifiScanResult) -> Unit
) {
    PairScaffold(
        title = "Choose your Wi-Fi network",
        subtitle = "Select the network the device will connect to",
        message = message,
        onClearMessage = onClearMessage
    ) {
        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        if (loading)
            FullscreenLoading()
        else
            NetworksList(list = list, onClick = onClick)
    }
}

@ExperimentalMaterialApi
@Composable
private fun NetworksList(
    list: List<WifiScanResults.WifiScanResult>,
    onClick: (network: WifiScanResults.WifiScanResult) -> Unit
) {
    LazyColumn(Modifier.padding(vertical = 8.dp)) {
        items(list) { network ->
            ListItem(
                text = { Text(network.ssid) },
                secondaryText = { Text(network.security)},
                icon = { StrengthIcon(network.bars) },
                modifier = Modifier.clickable(onClick = { onClick(network) }),
            )
        }
    }
}

@Composable
private fun StrengthIcon(bars: Int) {
    val resource = when(bars) {
        0 -> R.drawable.wifi_strength_1
        1 -> R.drawable.wifi_strength_2
        2 -> R.drawable.wifi_strength_3
        else -> R.drawable.wifi_strength_4
    }
    PreferenceIcon(
        painterResource(resource),
    )
}

@ExperimentalMaterialApi
@Composable
@Preview
private fun Preview() {
    AppTheme {
        Surface {
            SelectNetworkScreen(
                "", {},
                false,
                listOf(
                    WifiScanResults.WifiScanResult("Access Point", "", 1, -54, 3, "WPA2 Personal AES", ""),
                    WifiScanResults.WifiScanResult("Router", "", 1, 0, 2, "WPA2 Personal AES", ""),
                    WifiScanResults.WifiScanResult("Access Point Bedroom", "", 1, 0, 1, "WPA2 Personal AES", ""),
                    WifiScanResults.WifiScanResult("Access Point Living", "", 1, 0, 0, "WPA2 Personal AES", "")
                )
            ) {}
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview("Loading")
private fun PreviewLoading() {
    AppTheme {
        Surface {
            SelectNetworkScreen(
                "", {},
                true,
                listOf()
            ) {}
        }
    }
}