package org.alberto97.hisenseair.ui.pair

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.alberto97.hisenseair.ui.FullscreenLoading
import org.alberto97.hisenseair.viewmodels.PairViewModel

@Composable
fun ConnectingScreen(
    viewModel: PairViewModel,
    navigatePaired: () -> Unit,
    exit: () -> Unit
) {
    val message by viewModel.message.collectAsState()
    val step by viewModel.navAction.collectAsState(PairViewModel.NavAction.Connecting)

    LaunchedEffect(step) {
        when (step) {
            PairViewModel.NavAction.DevicePaired -> navigatePaired()
            PairViewModel.NavAction.Exit -> exit()
            else -> {}
        }
    }

    ConnectingScreen(
        message = message,
        onClearMessage = { viewModel.clearMessage() }
    )
}

@Composable
private fun ConnectingScreen(
    message: String,
    onClearMessage: () -> Unit
) {
    PairScaffold(
        title = "",
        subtitle = "",
        message = message,
        onClearMessage = onClearMessage
    ) {
        FullscreenLoading()
    }
}