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

object Routes {
    const val RegionPicker = "regionPicker"
    const val Login = "login"
    const val Main = "main"
    const val Pair = "pair"
    const val DeviceControl = "deviceControl"
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
            Routes.Main
        } else {
            Routes.RegionPicker
        }

        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = startDestination) {
                    composable(Routes.RegionPicker) { RegionScreen(navController) }
                    composable(Routes.Login) { LoginScreen(navController) }
                    composable(Routes.Main) { DevicesScreen(navController) }
                    composable(Routes.Pair) { PairScreen(navController) }
                    composable("${Routes.DeviceControl}/{dsn}",
                        deepLinks = listOf(navDeepLink { uriPattern = "${UriConstants.DEVICE_CONTROL}/{dsn}" })
                    ) { backStackEntry -> BuildDeviceControl(backStackEntry, displayInPanel)}
                }
            }
        }
    }

    @ExperimentalMaterialApi
    @Composable
    private fun BuildDeviceControl(backStackEntry: NavBackStackEntry, displayInPanel: Boolean) {
        val dsn = backStackEntry.arguments?.getString(DeviceControlParams.dsn)!!

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
