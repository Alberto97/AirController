package org.alberto97.hisenseair.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.os.bundleOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import org.alberto97.hisenseair.UIConstants
import org.alberto97.hisenseair.UriConstants
import org.alberto97.hisenseair.ui.devicecontrol.DeviceControlScreen
import org.alberto97.hisenseair.ui.devices.DevicesScreen
import org.alberto97.hisenseair.ui.login.LoginScreen
import org.alberto97.hisenseair.ui.pair.PairScreen
import org.alberto97.hisenseair.ui.region.RegionScreen
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

sealed class Screen(val route: String) {
    object RegionPicker: Screen("regionPicker")
    object Login: Screen("login")
    object Main: Screen("main")
    object Pair: Screen("pair")
    object DeviceControl: Screen("deviceControl/{dsn}") {
        object Params {
            const val dsn = "dsn"
        }
        fun createRoute(dsn: String) = "deviceControl/$dsn"
    }
}

class MainActivity : ComponentActivity() {

    private val viewModel: SplashViewModel by viewModel()

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayInPanel = intent.getBooleanExtra(UIConstants.EXTRA_USE_PANEL, false)

        val startDestination = if (viewModel.isLoggedIn()) {
            Screen.Main.route
        } else {
            Screen.RegionPicker.route
        }

        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = startDestination) {
                    composable(Screen.RegionPicker.route) {
                        RegionScreen(
                            openLogin = { navController.navigate(Screen.Login.route) }
                        )
                    }
                    composable(Screen.Login.route) {
                        LoginScreen(
                            openMain = { navController.navigate(Screen.Main.route) },
                            navigateUp = navController::navigateUp
                        )
                    }
                    composable(Screen.Main.route) {
                        DevicesScreen(
                            openDevice = { dsn ->
                                navController.navigate(Screen.DeviceControl.createRoute(dsn))
                             },
                            openLogin = { navController.navigate(Screen.Login.route) },
                            openPair = { navController.navigate(Screen.Pair.route) },
                            currentBackStackEntry = navController.currentBackStackEntry
                        )
                    }
                    composable(Screen.Pair.route) {
                        PairScreen(
                            popBackStack = navController::popBackStack,
                            previousBackStackEntry = navController.previousBackStackEntry
                        )
                    }
                    composable(
                        route = Screen.DeviceControl.route,
                        deepLinks = listOf(
                            navDeepLink { uriPattern = "${UriConstants.DEVICE_CONTROL}/{dsn}" }
                        )
                    ) { backStackEntry ->
                        val dsn = backStackEntry.arguments?.getString(Screen.DeviceControl.Params.dsn)
                        requireNotNull(dsn) { "Required parameter dsn not found" }

                        DeviceControlScreen(
                            dsn = dsn,
                            displayInPanel = displayInPanel,
                            navigateUp = navController::navigateUp,
                            navigateSettings = { navigateToSettings(dsn) }
                        )
                    }
                }
            }
        }
    }

    private fun navigateToSettings(dsn: String) {
        val intent = Intent(this, DeviceSettingsActivity::class.java)
        val bundle = bundleOf("dsn" to dsn)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
