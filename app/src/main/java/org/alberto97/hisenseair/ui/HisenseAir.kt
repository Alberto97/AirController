package org.alberto97.hisenseair.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import org.alberto97.hisenseair.ui.theme.AppTheme

@ExperimentalMaterialApi
@Composable
fun HisenseAir(
    displayInPanel: Boolean
) {
    AppTheme {
        NavGraph(
            displayInPanel = displayInPanel
        )
    }
}