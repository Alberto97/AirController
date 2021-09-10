package org.alberto97.hisenseair.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.SplashViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@Composable
fun HisenseAir(
    displayInPanel: Boolean,
    viewModel: SplashViewModel = getViewModel()
) {
    val startDestination = if (viewModel.isLoggedIn()) {
        Screen.Main.route
    } else {
        Screen.RegionPicker.route
    }

    AppTheme {
        NavGraph(
            displayInPanel = displayInPanel,
            startDestination = startDestination
        )
    }
}