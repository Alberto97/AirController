package org.alberto97.aircontroller.ui.common

import androidx.compose.foundation.layout.PaddingValues
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
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (contentPadding: PaddingValues) -> Unit
) {

    AppSnackbar(
        scaffoldState = scaffoldState,
        message = message,
        clearMessage = clearMessage
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = topBar,
        floatingActionButton = floatingActionButton,
    ) { contentPadding ->
        content(contentPadding)
    }
}