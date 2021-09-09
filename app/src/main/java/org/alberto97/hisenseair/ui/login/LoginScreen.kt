package org.alberto97.hisenseair.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ui.FullscreenLoading
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
    val scaffoldState =  rememberScaffoldState()

    val state by viewModel.state.collectAsState()
    LaunchedEffect(state) {
        when (state) {
            LoginState.Success -> openMain()
            LoginState.Error ->
                scaffoldState.snackbarHostState.showSnackbar("Login failed")
            else -> {}
        }
    }

    LoginScaffold(
        scaffoldState = scaffoldState,
        navigateUp = navigateUp
    ) {
        LoginContent(
            isLoading = state == LoginState.Loading,
            onLogin = { email, password -> viewModel.login(email, password) }
        )
    }
}

@Composable
private fun LoginScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navigateUp: () -> Unit,
    content: @Composable () -> Unit
) {
    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    navigationIcon = {
                        IconButton(
                            onClick = navigateUp,
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    }
                )
            },
            scaffoldState = scaffoldState
        ) {
            content()
        }
    }
}

@Composable
private fun LoginContent(
    onLogin: (email: String, password: String) -> Unit,
    isLoading: Boolean
) {
    val (password, setPassword) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }

    if (isLoading)
        FullscreenLoading()
    else
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            LoginTextField(
                value = email,
                onValueChange = setEmail,
                keyboardType = KeyboardType.Email,
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Email,
                        contentDescription = null
                    )
                }
            )
            LoginTextField(
                value = password,
                onValueChange = setPassword,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
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
    LoginScaffold(navigateUp = {}) {
        LoginContent(isLoading = false, onLogin = { _, _ -> })
    }
}