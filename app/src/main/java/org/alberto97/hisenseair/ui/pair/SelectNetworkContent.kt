package org.alberto97.hisenseair.ui.pair

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ayla.models.WifiScanResults
import org.alberto97.hisenseair.ui.FullscreenLoading
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.PairViewModel

@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun SelectNetworkContent(viewModel: PairViewModel) {
    val loading by viewModel.loading.collectAsState()
    val results by viewModel.scanResults.collectAsState()

    SelectNetworkContent(
        loading = loading,
        list = results,
        onClick = { viewModel.setSelectedSsid(it) }
    )
}

@ExperimentalMaterialApi
@Composable
private fun SelectNetworkContent(
    loading: Boolean,
    list: List<WifiScanResults.WifiScanResult>,
    onClick: (network: WifiScanResults.WifiScanResult) -> Unit
) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            "Choose your Wi-Fi network",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Text(
            "Select the network the device will connect to",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
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
    Icon(
        painterResource(resource),
        contentDescription = null,
        modifier = Modifier
            .size(40.dp)
            .padding(5.dp)
    )
}

@ExperimentalMaterialApi
@Composable
@Preview
private fun Preview() {
    AppTheme {
        Surface {
            SelectNetworkContent(
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
            SelectNetworkContent(
                true,
                listOf()
            ) {}
        }
    }
}