package org.alberto97.aircontroller.ui.common

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun OutlinedPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val (showPassword, setShowPassword) = remember { mutableStateOf(false) }
    val visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = { ShowPasswordIcon(showPassword, setShowPassword) }
    )
}

@Composable
private fun ShowPasswordIcon(visible: Boolean, setVisible: (visible: Boolean) -> Unit) {
    val icon = if (visible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility

    IconButton(
        onClick = { setVisible(!visible) }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}