package org.alberto97.aircontroller.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import org.alberto97.aircontroller.UIConstants

class MainActivity : ComponentActivity() {

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayInPanel = intent.getBooleanExtra(UIConstants.EXTRA_USE_PANEL, false)

        setContent {
            AirController(
                displayInPanel = displayInPanel
            )
        }
    }
}
