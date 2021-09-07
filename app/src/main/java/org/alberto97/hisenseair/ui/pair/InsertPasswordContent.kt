package org.alberto97.hisenseair.ui.pair

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.ui.FullscreenLoading
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.PairViewModel

@Composable
fun InsertPasswordContent(viewModel: PairViewModel) {
    val ssid by viewModel.selectedSsid.collectAsState()
    val loading by viewModel.loading.collectAsState()

    InsertPasswordContent(ssid, loading, onClick = {viewModel.connectDeviceToWifi(it)})
}

@Composable
private fun InsertPasswordContent(
    ssid: String,
    loading: Boolean,
    onClick: (password: String) -> Unit
) {


    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)) {
        Text(
            "Enter Wi-Fi password",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Text(
            ssid,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
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
        TextField(
            value = password,
            onValueChange = setPassword,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
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
            InsertPasswordContent("Test", false, {})
        }
    }
}

@Preview("Loading")
@Composable
private fun PreviewLoading() {
    AppTheme {
        Surface {
            InsertPasswordContent("Test", true, {})
        }
    }
}