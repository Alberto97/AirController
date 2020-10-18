package org.alberto97.hisenseair.ui.login

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.LoginViewModel

@ExperimentalMaterialApi
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onAuthenticated: () -> Unit
) {
    val scaffoldState =  rememberScaffoldState()
    val snackScope = rememberCoroutineScope()

    val isAuthenticated by viewModel.isAuthenticated.observeAsState()
    when (isAuthenticated) {
        false -> snackScope.launch {
            scaffoldState.snackbarHostState.showSnackbar("Login failed")
        }
        true -> onAuthenticated()
    }

    LoginContent(
        onLogin = { email, password -> viewModel.login(email, password) },
        scaffoldState = scaffoldState
    )
}

@Composable
private fun LoginContent(
    onLogin: (email: String, password: String) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val (password, setPassword) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }

    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar({ Text(stringResource(R.string.app_name)) })
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

@ExperimentalMaterialApi
@Preview
@Composable
private fun screenPreview() {
    LoginContent(onLogin = { _, _ -> })
}