package org.alberto97.aircontroller.ui.pair

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.aircontroller.ui.common.FullscreenLoading
import org.alberto97.aircontroller.ui.common.OutlinedPasswordField
import org.alberto97.aircontroller.ui.theme.AppTheme
import org.alberto97.aircontroller.viewmodels.PairViewModel

@Composable
fun InsertPasswordScreen(
    viewModel: PairViewModel,
    navigateConnecting: () -> Unit,
    exit: () -> Unit
) {

    val message by viewModel.message.collectAsState()
    val ssid by viewModel.selectedSsid.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val step by viewModel.navAction.collectAsState(PairViewModel.NavAction.InsertPassword)

    LaunchedEffect(step) {
        when (step) {
            PairViewModel.NavAction.Connecting  -> navigateConnecting()
            PairViewModel.NavAction.Exit -> exit()
            else -> {}
        }
    }

    InsertPasswordScreen(
        message = message,
        onClearMessage = { viewModel.clearMessage() },
        ssid = ssid,
        loading = loading,
        onClick = {viewModel.connectDeviceToWifi(it)})
}

@Composable
private fun InsertPasswordScreen(
    message: String,
    onClearMessage: () -> Unit,
    ssid: String,
    loading: Boolean,
    onClick: (password: String) -> Unit
) {
    PairScaffold(
        title = "Enter Wi-Fi password",
        subtitle = ssid,
        message = message,
        onClearMessage = onClearMessage
    ) {
        Spacer(Modifier.padding(18.dp))

        if (loading)
            FullscreenLoading()
        else
            Content(onClick = onClick)
    }
}

@Composable
private fun Content(onClick: (password: String) -> Unit) {
    val (password, setPassword) = remember { mutableStateOf("")}
    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
        OutlinedPasswordField(
            value = password,
            onValueChange = setPassword,
            label = { Text("Password") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Button(onClick = {onClick(password)}, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(text = "Confirm")
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Surface {
            InsertPasswordScreen("", {}, "Test", false, {})
        }
    }
}

@Preview("Loading")
@Composable
private fun PreviewLoading() {
    AppTheme {
        Surface {
            InsertPasswordScreen("", {}, "Test", true, {})
        }
    }
}