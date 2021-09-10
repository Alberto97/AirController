package org.alberto97.hisenseair.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.os.bundleOf
import org.alberto97.hisenseair.UIConstants
import org.alberto97.hisenseair.ui.devicesettings.DeviceSettingsActivity

class MainActivity : ComponentActivity() {

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayInPanel = intent.getBooleanExtra(UIConstants.EXTRA_USE_PANEL, false)

        setContent {
            HisenseAir(
                displayInPanel = displayInPanel,
                openDeviceSettings = { dsn -> navigateToSettings(dsn) }
            )
        }
    }

    private fun navigateToSettings(dsn: String) {
        val bundle = bundleOf("dsn" to dsn)
        val intent = Intent(this, DeviceSettingsActivity::class.java).also {
            it.putExtras(bundle)
        }
        startActivity(intent)
    }
}
