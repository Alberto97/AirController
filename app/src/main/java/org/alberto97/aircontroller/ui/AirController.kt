package org.alberto97.aircontroller.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import org.alberto97.aircontroller.ui.theme.AppTheme

@ExperimentalMaterialApi
@Composable
fun AirController(
    displayInPanel: Boolean
) {
    AppTheme {
        NavGraph(
            displayInPanel = displayInPanel
        )
    }
}