package org.alberto97.hisenseair.ui.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun LoginButton(
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
    ) {
        Text("Login", modifier = Modifier.padding(vertical = 2.dp))
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        LoginButton(onClick = {})
    }
}