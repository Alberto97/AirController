package org.alberto97.aircontroller.utils

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences

object DataStoreExtensions {

    // There apparently is an issue with MutablePreferences.set.
    // The signature does not allow a nullable value but the source code actually handles that
    fun <T> MutablePreferences.setOrRemove(key: Preferences.Key<T>, value: T?) {
        if (value == null)
            this.remove(key)
        else
            this[key] = value
    }
}