package org.alberto97.aircontroller.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.alberto97.aircontroller.UriConstants
import org.alberto97.aircontroller.ui.about.AboutScreen
import org.alberto97.aircontroller.ui.devicecontrol.DeviceControlScreen
import org.alberto97.aircontroller.ui.devices.DevicesScreen
import org.alberto97.aircontroller.ui.devicesettings.DeviceSettingsScreen
import org.alberto97.aircontroller.ui.login.LoginScreen
import org.alberto97.aircontroller.ui.oob.OobScreen
import org.alberto97.aircontroller.ui.pair.*
import org.alberto97.aircontroller.ui.splash.SplashScreen
import org.alberto97.aircontroller.ui.pair.PairViewModel
import org.koin.androidx.compose.getViewModel


sealed class Screen(val route: String) {
    object Splash: Screen("splash")
    object Oob: Screen("oob")
    object Login: Screen("login")
    object Main: Screen("main")
    object About: Screen("about")
    object Pair: Screen("pair")
    object DeviceControl: Screen("deviceControl/{dsn}") {
        object Params {
            const val dsn = "dsn"
        }
        fun createRoute(dsn: String) = "deviceControl/$dsn"
    }
    object DeviceSettings: Screen("deviceSettings/{dsn}") {
        object Params {
            const val dsn = "dsn"
        }
        fun createRoute(dsn: String) = "deviceSettings/$dsn"
    }
}

sealed class PairScreen(val route: String) {
    object PickDevice: PairScreen("pickDevice")
    object SelectNetwork: PairScreen("selectNetwork")
    object InsertPassword: PairScreen("insertPassword")
    object Connecting: PairScreen("connecting")
    object DevicePaired: PairScreen("devicePaired")
}

@Composable
fun NavGraph(
    displayInPanel: Boolean,
    startDestination: String = Screen.Splash.route,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController, startDestination = startDestination) {
        composable(Screen.Splash.route) {
            SplashScreen(
                openLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(startDestination) { inclusive = true }
                    }
                },
                openMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(startDestination) { inclusive = true }
                    }
                },
                openOob = {
                    navController.navigate(Screen.Oob.route) {
                        popUpTo(startDestination) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Oob.route) {
            OobScreen(
                openLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Oob.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                openMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
               }
            )
        }
        composable(Screen.Main.route) {
            DevicesScreen(
                openAbout = {
                    navController.navigate(Screen.About.route)
                },
                openDevice = { dsn ->
                    navController.navigate(Screen.DeviceControl.createRoute(dsn))
                },
                openPair = { navController.navigate(Screen.Pair.route) },
                openLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                currentBackStackEntry = navController.currentBackStackEntry
            )
        }
        composable(Screen.About.route) {
            AboutScreen(navigateUp = { navController.popBackStack() })
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
                navigateSettings = { navController.navigate(Screen.DeviceSettings.createRoute(dsn)) }
            )
        }
        composable(Screen.DeviceSettings.route) { backStackEntry ->
            val dsn = backStackEntry.arguments?.getString(Screen.DeviceSettings.Params.dsn)
            requireNotNull(dsn) { "Required parameter dsn not found" }

            DeviceSettingsScreen(
                dsn = dsn,
                navigateUp = navController::navigateUp,
                navigateHome = { navController.popBackStack(Screen.Main.route, false) },
                homeBackStackEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.Main.route)
                }
            )
        }
        addPairGraph(navController)
    }
}

@Composable
private fun NavController.getPairViewModel(backStackEntry: NavBackStackEntry): PairViewModel {
    val pairBackStackEntry = remember(backStackEntry) {
        getBackStackEntry(Screen.Pair.route)
    }
    return getViewModel(viewModelStoreOwner = pairBackStackEntry)
}

private fun NavGraphBuilder.addPairGraph(navController: NavController) {
    navigation(
        route = Screen.Pair.route,
        startDestination = PairScreen.PickDevice.route
    ) {
        composable(PairScreen.PickDevice.route) { backStackEntry ->
            val viewModel: PairViewModel = navController.getPairViewModel(backStackEntry)
            PickDeviceScreen(
                viewModel = viewModel,
                exit = navController::popBackStack,
                navigateNetwork = {
                    navController.navigate(PairScreen.SelectNetwork.route)
                }
            )
        }
        composable(PairScreen.SelectNetwork.route) { backStackEntry ->
            val viewModel: PairViewModel = navController.getPairViewModel(backStackEntry)
            SelectNetworkScreen(
                viewModel = viewModel,
                exit = navController::popBackStack,
                navigatePassword = {
                    navController.navigate(PairScreen.InsertPassword.route)
                }
            )
        }
        composable(PairScreen.InsertPassword.route) { backStackEntry ->
            val viewModel: PairViewModel = navController.getPairViewModel(backStackEntry)
            InsertPasswordScreen(
                viewModel = viewModel,
                exit = navController::popBackStack,
                navigateConnecting = {
                    navController.navigate(PairScreen.Connecting.route) {
                        popUpTo(PairScreen.PickDevice.route)
                    }
                }
            )
        }
        composable(PairScreen.Connecting.route) { backStackEntry ->
            val viewModel: PairViewModel = navController.getPairViewModel(backStackEntry)
            ConnectingScreen(
                viewModel = viewModel,
                exit = navController::popBackStack,
                navigatePaired = {
                    navController.navigate(PairScreen.DevicePaired.route) {
                        popUpTo(PairScreen.PickDevice.route) { inclusive = true }
                    }
                }
            )
        }
        composable(PairScreen.DevicePaired.route) { backStackEntry ->
            val viewModel: PairViewModel = navController.getPairViewModel(backStackEntry)
            DevicePairedScreen(
                viewModel = viewModel,
                previousBackStackEntry = navController.previousBackStackEntry,
                finish = navController::popBackStack
            )
        }
    }
}
