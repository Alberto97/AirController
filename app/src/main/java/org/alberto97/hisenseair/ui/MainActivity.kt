package org.alberto97.hisenseair.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import org.alberto97.hisenseair.UIConstants
import org.alberto97.hisenseair.UriConstants
import org.alberto97.hisenseair.koin.getNavGraphViewModel
import org.alberto97.hisenseair.ui.devicecontrol.DeviceControlScreen
import org.alberto97.hisenseair.ui.devices.DevicesScreen
import org.alberto97.hisenseair.ui.login.LoginScreen
import org.alberto97.hisenseair.ui.pair.*
import org.alberto97.hisenseair.ui.region.RegionScreen
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.PairViewModel
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

sealed class PairScreen(val route: String) {
    object PickDevice: PairScreen("pickDevice")
    object SelectNetwork: PairScreen("selectNetwork")
    object InsertPassword: PairScreen("insertPassword")
    object Connecting: PairScreen("connecting")
    object DevicePaired: PairScreen("devicePaired")
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
                    navigation(
                        route = Screen.Pair.route,
                        startDestination = PairScreen.PickDevice.route
                    ) {
                        composable(PairScreen.PickDevice.route) {
                            val viewModel: PairViewModel = getNavGraphViewModel(
                                backStackEntry = navController.getBackStackEntry(Screen.Pair.route)
                            )
                            PickDeviceScreen(
                                viewModel = viewModel,
                                exit = navController::popBackStack,
                                navigateNetwork = {
                                    navController.navigate(PairScreen.SelectNetwork.route)
                                })
                        }
                        composable(PairScreen.SelectNetwork.route) {
                            val viewModel: PairViewModel = getNavGraphViewModel(
                                backStackEntry = navController.getBackStackEntry(Screen.Pair.route)
                            )
                            SelectNetworkScreen(
                                viewModel = viewModel,
                                exit = navController::popBackStack,
                                navigatePassword = {
                                    navController.navigate(PairScreen.InsertPassword.route)
                                })
                        }
                        composable(PairScreen.InsertPassword.route) {
                            val viewModel: PairViewModel = getNavGraphViewModel(
                                backStackEntry = navController.getBackStackEntry(Screen.Pair.route)
                            )
                            InsertPasswordScreen(
                                viewModel = viewModel,
                                exit = navController::popBackStack,
                                navigateConnecting = {
                                    navController.navigate(PairScreen.Connecting.route) {
                                        popUpTo(PairScreen.PickDevice.route)
                                }
                            })
                        }
                        composable(PairScreen.Connecting.route) {
                            val viewModel: PairViewModel = getNavGraphViewModel(
                                backStackEntry = navController.getBackStackEntry(Screen.Pair.route)
                            )
                            ConnectingScreen(
                                viewModel = viewModel,
                                exit = navController::popBackStack,
                                navigatePaired = {
                                    navController.navigate(PairScreen.DevicePaired.route) {
                                        popUpTo(PairScreen.PickDevice.route) { inclusive = true }
                                }
                            })
                        }
                        composable(PairScreen.DevicePaired.route) {
                            // https://stackoverflow.com/questions/68738304/jetpack-compose-navigation-problems-share-viewmodel-in-nested-graph
                            if (it.lifecycleIsResumed()) {
                                val viewModel: PairViewModel = getNavGraphViewModel(
                                    backStackEntry = navController.getBackStackEntry(Screen.Pair.route)
                                )
                                DevicePairedScreen(
                                    viewModel = viewModel,
                                    previousBackStackEntry = navController.previousBackStackEntry,
                                    finish = navController::popBackStack
                                )
                            }
                        }
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

    /**
     * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
     *
     * This is used to de-duplicate navigation events.
     */
    private fun NavBackStackEntry.lifecycleIsResumed() =
        this.lifecycle.currentState == Lifecycle.State.RESUMED
}
