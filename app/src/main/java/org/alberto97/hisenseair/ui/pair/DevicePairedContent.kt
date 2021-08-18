package org.alberto97.hisenseair.ui.pair

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MobileFriendly
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.PairViewModel

@Composable
fun DevicePairedContent(viewModel: PairViewModel, onClick: () -> Unit) {
    val deviceName by viewModel.deviceName.collectAsState()

    DevicePairedContent(onClick, deviceName)
}

@Composable
private fun DevicePairedContent(onClick: () -> Unit, deviceName: String) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)) {

        Text(
            "Device connected successfully",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Text(deviceName,
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
                Text("Exit")
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface{
            DevicePairedContent({}, "Hi-Smart-serialNumber")
        }
    }
}