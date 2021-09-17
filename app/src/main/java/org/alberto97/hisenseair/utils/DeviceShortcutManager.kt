package org.alberto97.hisenseair.utils

import android.app.Application
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.UriConstants
import org.alberto97.hisenseair.ui.MainActivity

interface IDeviceShortcutManager {
    fun createShortcut(deviceName: String, id: String)
    fun updateShortcut(deviceName: String, id: String)
    fun removeShortcut(id: String)
}

class DeviceShortcutManager(private val app: Application): IDeviceShortcutManager {

    private fun buildShortcut(deviceName: String, id: String): ShortcutInfoCompat {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "${UriConstants.DEVICE_CONTROL}/$id".toUri(),
            app,
            MainActivity::class.java
        )

        return ShortcutInfoCompat.Builder(app, id)
            .setShortLabel(deviceName)
            .setLongLabel(deviceName)
            .setRank(0)
            .setIcon(IconCompat.createWithResource(app, R.drawable.ic_fan_primary))
            .setIntent(intent)
            .build()
    }

    override fun createShortcut(deviceName: String, id: String) {
        val shortcut = buildShortcut(deviceName, id)
        ShortcutManagerCompat.pushDynamicShortcut(app, shortcut)
    }

    override fun updateShortcut(deviceName: String, id: String) {
        val shortcut = buildShortcut(deviceName, id)
        ShortcutManagerCompat.updateShortcuts(app, listOf(shortcut))
    }

    override fun removeShortcut(id: String) {
        ShortcutManagerCompat.removeDynamicShortcuts(app, listOf(id))
    }
}