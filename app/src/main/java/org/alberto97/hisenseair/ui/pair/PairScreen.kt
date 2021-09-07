package org.alberto97.hisenseair.ui.pair

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import org.alberto97.hisenseair.ui.devices.DevicesStateHandleParams
import org.alberto97.hisenseair.viewmodels.PairViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@Composable
fun PairScreen(
    navController: NavController,
    viewModel: PairViewModel = getViewModel()
) {
    val scaffoldState =  rememberScaffoldState()
    val step by viewModel.currentStep.collectAsState()
    val message by viewModel.messages.collectAsState()
    val exit by viewModel.exit.collectAsState()

    if (message.isNotEmpty())
        LaunchedEffect(scaffoldState.snackbarHostState) {
            val result = scaffoldState.snackbarHostState.showSnackbar(message)
            if (result == SnackbarResult.Dismissed)
                viewModel.clearMessage()
        }

    fun back(needsRefresh: Boolean = true) {
        val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(DevicesStateHandleParams.needsRefresh, needsRefresh)
        navController.popBackStack()
    }

    if (exit)
        back(false)

    Scaffold(scaffoldState = scaffoldState) {
        when(step) {
            PairViewModel.Steps.PickDevice -> PickDeviceContent(viewModel)
            PairViewModel.Steps.SelectNetwork -> SelectNetworkContent(viewModel)
            PairViewModel.Steps.InsertPassword -> InsertPasswordContent(viewModel)
            PairViewModel.Steps.Connecting -> ConnectingContent()
            else -> DevicePairedContent(viewModel, onClick = { back() })
        }
    }
}