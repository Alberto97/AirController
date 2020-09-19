package org.alberto97.hisenseair.ui.login

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.ui.theme.AppTheme


@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier,
) {
    val label = when (keyboardType) {
        KeyboardType.Email -> "Email address"
        KeyboardType.Password -> "Password"
        else -> null
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { if (label != null) Text(label) },
        leadingIcon = leadingIcon,
        keyboardType = keyboardType,
        visualTransformation = visualTransformation,
        modifier = modifier.fillMaxWidth().padding(vertical = 12.dp)
    )
}

@Preview
@Composable
private fun preview() {
    AppTheme {
        LoginTextField(value = "Test", onValueChange = {})
    }
}