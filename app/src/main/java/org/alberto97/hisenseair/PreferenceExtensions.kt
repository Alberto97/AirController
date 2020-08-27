package org.alberto97.hisenseair

import androidx.preference.SwitchPreference

object PreferenceExtensions {
    fun SwitchPreference.setCheckedIfVisible(value: Boolean?) {
        this.isVisible = value != null
        if (value != null)
            this.isChecked = value
    }
}