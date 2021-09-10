package org.alberto97.hisenseair.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.alberto97.hisenseair.UriConstants
import org.alberto97.hisenseair.koin.getNavGraphViewModel
import org.alberto97.hisenseair.ui.devicecontrol.DeviceControlScreen
import org.alberto97.hisenseair.ui.devices.DevicesScreen
import org.alberto97.hisenseair.ui.devicesettings.DeviceSettingsScreen
import org.alberto97.hisenseair.ui.login.LoginScreen
import org.alberto97.hisenseair.ui.pair.*
import org.alberto97.hisenseair.ui.region.RegionScreen
import org.alberto97.hisenseair.viewmodels.PairViewModel


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

@ExperimentalMaterialApi
@Composable
fun NavGraph(
    displayInPanel: Boolean,
    startDestination: String = Screen.Login.route,
    navController: NavHostController = rememberNavController(),
) {
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
                homeBackStackEntry = remember {
                    navController.getBackStackEntry(Screen.Main.route)
                }
            )
        }
        addPairGraph(navController)
    }
}

@Composable
private fun getPairViewModel(navController: NavController): PairViewModel {
    return getNavGraphViewModel(
        backStackEntry = remember {
            navController.getBackStackEntry(Screen.Pair.route)
        }
    )
}

@ExperimentalMaterialApi
private fun NavGraphBuilder.addPairGraph(navController: NavController) {
    navigation(
        route = Screen.Pair.route,
        startDestination = PairScreen.PickDevice.route
    ) {
        composable(PairScreen.PickDevice.route) {
            val viewModel: PairViewModel = getPairViewModel(navController)
            PickDeviceScreen(
                viewModel = viewModel,
                exit = navController::popBackStack,
                navigateNetwork = {
                    navController.navigate(PairScreen.SelectNetwork.route)
                }
            )
        }
        composable(PairScreen.SelectNetwork.route) {
            val viewModel: PairViewModel = getPairViewModel(navController)
            SelectNetworkScreen(
                viewModel = viewModel,
                exit = navController::popBackStack,
                navigatePassword = {
                    navController.navigate(PairScreen.InsertPassword.route)
                }
            )
        }
        composable(PairScreen.InsertPassword.route) {
            val viewModel: PairViewModel = getPairViewModel(navController)
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
        composable(PairScreen.Connecting.route) {
            val viewModel: PairViewModel = getPairViewModel(navController)
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
        composable(PairScreen.DevicePaired.route) {
            val viewModel: PairViewModel = getPairViewModel(navController)
            DevicePairedScreen(
                viewModel = viewModel,
                previousBackStackEntry = navController.previousBackStackEntry,
                finish = navController::popBackStack
            )
        }
    }
}
