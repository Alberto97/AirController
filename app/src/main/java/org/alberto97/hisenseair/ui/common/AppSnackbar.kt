package org.alberto97.hisenseair.ui.common

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun AppSnackbar(
    message: String?,
    clearMessage: () -> Unit,
    scaffoldState: ScaffoldState
) {

    LaunchedEffect(message) {
        if (!message.isNullOrEmpty()) {
            val result = scaffoldState.snackbarHostState.showSnackbar(message)
            if (result == SnackbarResult.Dismissed)
                clearMessage()
        }
    }
}