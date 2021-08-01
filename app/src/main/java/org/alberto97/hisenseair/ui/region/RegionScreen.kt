package org.alberto97.hisenseair.ui.region

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.alberto97.hisenseair.repositories.Region
import org.alberto97.hisenseair.ui.Routes
import org.alberto97.hisenseair.viewmodels.RegionViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun RegionScreen(
    navController: NavController,
    viewModel: RegionViewModel = getViewModel()
) {
    viewModel.setRegion(Region.EU)
    navController.navigate(Routes.Login)
}