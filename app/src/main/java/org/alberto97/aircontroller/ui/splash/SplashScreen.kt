package org.alberto97.aircontroller.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.getViewModel

@Composable
fun SplashScreen(
    openLogin: () -> Unit,
    openMain: () -> Unit,
    openOob: () -> Unit,
    viewModel: SplashViewModel = getViewModel()
) {
    val state by viewModel.navAction.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            SplashViewModel.SplashNavAction.Login -> openLogin()
            SplashViewModel.SplashNavAction.Main -> openMain()
            SplashViewModel.SplashNavAction.Oob -> openOob()
            else -> {}
        }
    }
}
