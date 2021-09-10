package org.alberto97.hisenseair.ui.common

import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable

@Composable
fun AppScaffold(
    message: String? = null,
    clearMessage: () -> Unit = {},
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {

    AppSnackbar(
        scaffoldState = scaffoldState,
        message = message,
        clearMessage = clearMessage
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = topBar,
    ) {
        content()
    }
}