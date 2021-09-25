package org.alberto97.aircontroller

object UriConstants {
    const val DEVICE_CONTROL = "aircontroller://deviceControl"
}

object UIConstants {
    /*
     * https://github.com/aosp-mirror/platform_frameworks_base/blob/android11-release/packages/SystemUI/src/com/android/systemui/controls/ui/DetailDialog.kt#L49
     * Indicate to the activity that it is being rendered in a bottomsheet, and they
     * should optimize the layout for a smaller space.
     */
    const val EXTRA_USE_PANEL = "controls.DISPLAY_IN_PANEL"
}