package org.alberto97.hisenseair

object BottomSheetFragments {
    const val MODE = "WorkModeSheet"
    const val FAN = "FanSpeedSheet"
    const val TEMP = "TempSheet"
}

object PreferenceConstants {
    const val PREFERENCE_AMBIENT_TEMP = "ambientTemp"
    const val PREFERENCE_TEMP_CONTROL = "tempControl"
    const val PREFERENCE_MODE = "mode"
    const val PREFERENCE_FAN_SPEED = "fan"
    const val PREFERENCE_NIGHT = "night"
    const val PREFERENCE_SETTINGS = "settings"
    const val PREFERENCE_POWER = "power"
    const val PREFERENCE_CATEGORY_AIRFLOW = "airFlow"
    const val PREFERENCE_AIRFLOW_HORIZONTAL = "horizontal"
    const val PREFERENCE_AIRFLOW_VERTICAL = "vertical"
    const val PREFERENCE_CATEGORY_ADVANCED = "advanced"
    const val PREFERENCE_ADVANCED_BACKLIGHT = "backlight"
    const val PREFERENCE_ADVANCED_ECO = "eco"
    const val PREFERENCE_ADVANCED_QUIET = "quiet"
    const val PREFERENCE_ADVANCED_BOOST = "boost"
}

object UIConstants {
    /*
     * https://github.com/aosp-mirror/platform_frameworks_base/blob/android11-release/packages/SystemUI/src/com/android/systemui/controls/ui/DetailDialog.kt#L49
     * Indicate to the activity that it is being rendered in a bottomsheet, and they
     * should optimize the layout for a smaller space.
     */
    const val EXTRA_USE_PANEL = "controls.DISPLAY_IN_PANEL"
}