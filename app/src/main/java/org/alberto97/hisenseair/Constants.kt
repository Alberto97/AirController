package org.alberto97.hisenseair

object BottomSheetFragments {
    const val MODE = "WorkModeSheet"
    const val FAN = "FanSpeedSheet"
    const val TEMP = "TempSheet"
}

object UIConstants {
    /*
     * https://github.com/aosp-mirror/platform_frameworks_base/blob/android11-release/packages/SystemUI/src/com/android/systemui/controls/ui/DetailDialog.kt#L49
     * Indicate to the activity that it is being rendered in a bottomsheet, and they
     * should optimize the layout for a smaller space.
     */
    const val EXTRA_USE_PANEL = "controls.DISPLAY_IN_PANEL"
}