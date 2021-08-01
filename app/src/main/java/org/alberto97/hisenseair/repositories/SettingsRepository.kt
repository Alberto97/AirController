package org.alberto97.hisenseair.repositories

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


enum class Region {
    EU, US
}

interface ISettingsRepository {
    var region: Region?
    var loggedIn: Boolean
}

class SettingsRepository(private val app: Application): ISettingsRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val regionKey = stringPreferencesKey("region")
    private val loggedInKey = booleanPreferencesKey("logged_in")

    private var _region = getReg()
    override var region: Region?
        get() = _region
        set(value) = setReg(value)

    private var _loggedIn = getLogged()
    override var loggedIn: Boolean
        get() = _loggedIn
        set(value) = setLogged(value)

    private fun getReg(): Region? {
        val str = runBlocking {
            app.dataStore.data.map { settings -> settings[regionKey] }.first()
        }
        str ?: return null
        return Region.valueOf(str)
    }

    private fun setReg(region: Region?) {
        runBlocking {
            app.dataStore.edit { settings -> settings[regionKey] = region.toString() }
        }
        _region = region
    }

    private fun getLogged(): Boolean {
        return runBlocking {
            app.dataStore.data.map { settings -> settings[loggedInKey] ?: false }.first()
        }
    }

    private fun setLogged(loggedIn: Boolean) {
        runBlocking {
            app.dataStore.edit { settings -> settings[loggedInKey] = loggedIn }
        }
        _loggedIn = loggedIn
    }
}