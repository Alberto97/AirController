package org.alberto97.hisenseair.ui.login

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.alberto97.hisenseair.ui.theme.AppTheme

@Composable
fun LoginScreen(
    onLogin: (email: String, password: String) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val (password, setPassword) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }

    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar({ Text("HisenseAir") })
            },
            scaffoldState = scaffoldState
        ) {
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                LoginTextField(
                    value = email,
                    onValueChange = setEmail,
                    keyboardType = KeyboardType.Email,
                    leadingIcon = { Icon(Icons.Outlined.Email) }
                )
                LoginTextField(
                    value = password,
                    onValueChange = setPassword,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Outlined.Lock) }
                )
                LoginButton(
                    enabled = email.isNotEmpty() && password.isNotEmpty(),
                    onClick = { onLogin(email, password) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun screenPreview() {
    LoginScreen(onLogin = { _, _ -> })
}