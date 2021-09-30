package org.alberto97.aircontroller.ui.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.ui.theme.AppTheme

@Composable
fun LoginButton(
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(stringResource(R.string.login), modifier = Modifier.padding(vertical = 2.dp))
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        LoginButton(onClick = {})
    }
}