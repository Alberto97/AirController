package org.alberto97.hisenseair.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.navigation.NavBackStackEntry
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
        fun createRoute(dsn: String) = "deviceControl/$dsn"
    }
}

object DeviceControlParams {
    const val dsn = "dsn"
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
                    composable(Screen.RegionPicker.route) { RegionScreen(navController) }
                    composable(Screen.Login.route) { LoginScreen(navController) }
                    composable(Screen.Main.route) { DevicesScreen(navController) }
                    composable(Screen.Pair.route) { PairScreen(navController) }
                    composable(
                        route = Screen.DeviceControl.route,
                        deepLinks = listOf(navDeepLink { uriPattern = "${UriConstants.DEVICE_CONTROL}/{dsn}" })
                    ) { backStackEntry -> BuildDeviceControl(backStackEntry, displayInPanel)}
                }
            }
        }
    }

    @ExperimentalMaterialApi
    @Composable
    private fun BuildDeviceControl(backStackEntry: NavBackStackEntry, displayInPanel: Boolean) {
        val dsn = backStackEntry.arguments?.getString(DeviceControlParams.dsn)
        requireNotNull(dsn) { "Required parameter dsn not found" }
        DeviceControlScreen(
            dsn,
            displayInPanel,
            { navigateToSettings(dsn) }
        )
    }

    private fun navigateToSettings(dsn: String) {
        val intent = Intent(this, DeviceSettingsActivity::class.java)
        val bundle = bundleOf("dsn" to dsn)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
