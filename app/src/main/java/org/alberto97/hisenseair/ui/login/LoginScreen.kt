package org.alberto97.hisenseair.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.common.AppScaffold
import org.alberto97.hisenseair.ui.common.AppToolbar
import org.alberto97.hisenseair.ui.common.FullscreenLoading
import org.alberto97.hisenseair.ui.common.OutlinedPasswordField
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.LoginState
import org.alberto97.hisenseair.viewmodels.LoginViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(
    navigateUp: () -> Unit,
    openMain: () -> Unit,
    viewModel: LoginViewModel = getViewModel()
) {
    val state by viewModel.state.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            LoginState.Success -> openMain()
            else -> {}
        }
    }

    LoginScreen(
        message = message,
        clearMessage = { viewModel.clearMessage() },
        state = state,
        navigateUp = navigateUp,
        onLogin = { email, password -> viewModel.login(email, password) }
    )
}

@Composable
private fun LoginScreen(
    message: String,
    clearMessage: () -> Unit,
    state: LoginState,
    navigateUp: () -> Unit,
    onLogin: (email: String, password: String) -> Unit
) {
    val (password, setPassword) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }

    AppScaffold(
        message = message,
        clearMessage = clearMessage,
        topBar = {
            AppToolbar(
                title = { Text(stringResource(R.string.app_name)) },
                navigateUp = navigateUp
            )
        }
    ) {
        if (state == LoginState.Loading)
            FullscreenLoading()
        else
            LoginContent(
                password = password,
                setPassword = setPassword,
                email = email,
                setEmail = setEmail,
                onLogin = onLogin
            )
    }
}

@Composable
private fun LoginContent(
    password: String,
    setPassword: (value: String) -> Unit,
    email: String,
    setEmail: (value: String) -> Unit,
    onLogin: (email: String, password: String) -> Unit
) {
    val modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = setEmail,
            label = { Text("Email address") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = modifier,
            leadingIcon = {
                Icon(
                    Icons.Outlined.Email,
                    contentDescription = null
                )
            }
        )
        OutlinedPasswordField(
            value = password,
            onValueChange = setPassword,
            label = { Text("Password") },
            modifier = modifier,
            leadingIcon = {
                Icon(
                    Icons.Outlined.Lock,
                    contentDescription = null
                )
            }
        )
        LoginButton(
            enabled = email.isNotEmpty() && password.isNotEmpty(),
            onClick = { onLogin(email, password) }
        )
    }
}

@Preview
@Composable
private fun ScreenPreview() {
    AppTheme {
        LoginScreen("", {}, LoginState.None, {}, {_, _ ->})
    }
}