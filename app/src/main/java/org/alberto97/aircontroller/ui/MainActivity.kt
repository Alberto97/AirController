package org.alberto97.aircontroller.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.alberto97.aircontroller.App
import org.alberto97.aircontroller.UIConstants

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val displayInPanel = intent.getBooleanExtra(UIConstants.EXTRA_USE_PANEL, false)

        val app = application as App
        splashScreen.setKeepOnScreenCondition {
            app.showSplashScreen
        }

        setContent {
            AirController(
                displayInPanel = displayInPanel
            )
        }
    }
}
