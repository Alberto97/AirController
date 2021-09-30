package org.alberto97.aircontroller.ui.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alberto97.aircontroller.R
import org.alberto97.aircontroller.ui.theme.AppTheme
import org.alberto97.aircontroller.viewmodels.SplashViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SplashScreen(
    openLogin: () -> Unit,
    openMain: () -> Unit,
    openOob: () -> Unit,
    viewModel: SplashViewModel = getViewModel()
) {
    val showOfflineMessage by viewModel.showOfflineMessage.collectAsState()
    val state by viewModel.navAction.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            SplashViewModel.SplashNavAction.Login -> openLogin()
            SplashViewModel.SplashNavAction.Main -> openMain()
            SplashViewModel.SplashNavAction.Oob -> openOob()
            else -> {}
        }
    }

    SplashScreen(
        showOfflineMessage = showOfflineMessage,
        retry = { viewModel.load() }
    )
}

@Composable
private fun SplashScreen(
    showOfflineMessage: Boolean,
    retry: () -> Unit
) {

    Scaffold {
        Box {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    painter = painterResource(R.drawable.ic_weather_windy),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                if (showOfflineMessage)
                    Snackbar(
                        action = {
                            TextButton(
                                onClick = retry,
                                content = { Text(stringResource(R.string.splash_offline_action)) }
                            )
                        }
                    ) {
                        Text(stringResource(R.string.splash_offline_message))
                    }
            }

        }
    }
}

@Composable
@Preview
private fun Preview() {
    AppTheme {
        SplashScreen(true, {})
    }
}