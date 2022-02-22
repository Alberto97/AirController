package org.alberto97.aircontroller.ui.about

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import org.alberto97.aircontroller.BuildConfig

class AboutViewModel(private val app: Application) : ViewModel() {
    val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

    fun openRepository() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://github.com/Alberto97/AirController")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        app.startActivity(intent)
    }
}