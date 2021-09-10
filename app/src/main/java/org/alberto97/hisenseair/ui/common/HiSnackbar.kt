package org.alberto97.hisenseair.ui

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun HiSnackbar(
    message: String?,
    onClearMessage: () -> Unit,
    scaffoldState: ScaffoldState) {

    LaunchedEffect(message) {
        if (!message.isNullOrEmpty()) {
            val result = scaffoldState.snackbarHostState.showSnackbar(message)
            if (result == SnackbarResult.Dismissed)
                onClearMessage()
        }
    }
}