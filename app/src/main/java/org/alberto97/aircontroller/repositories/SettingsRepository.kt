package org.alberto97.aircontroller.repositories

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
import org.alberto97.aircontroller.common.enums.Region
import org.alberto97.aircontroller.utils.DataStoreExtensions.setOrRemove
import org.alberto97.aircontroller.common.enums.Provider
import org.alberto97.aircontroller.common.repositories.ISettingsRepository


class SettingsRepository(private val app: Application): ISettingsRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val oobKey = booleanPreferencesKey("oob")
    private val regionKey = stringPreferencesKey("region")
    private val loggedInKey = booleanPreferencesKey("logged_in")
    private val providerKey = stringPreferencesKey("provider")

    private var _oob = getOutOfBox()
    override var oob: Boolean
        get() = _oob
        set(value) = setOutOfBox(value)

    private var _region = getReg()
    override var region: Region?
        get() = _region
        set(value) = setReg(value)

    private var _loggedIn = getLogged()
    override var loggedIn: Boolean
        get() = _loggedIn
        set(value) = setLogged(value)

    private var _provider = getProv()
    override var provider: Provider?
        get() = _provider
        set(value) = setProv(value)

    private fun getOutOfBox(): Boolean {
        return runBlocking {
            app.dataStore.data.map { settings -> settings[oobKey] ?: true }.first()
        }
    }

    private fun setOutOfBox(value: Boolean) {
        runBlocking {
            app.dataStore.edit { settings -> settings[oobKey] = value }
        }
        _oob = value
    }

    private fun getReg(): Region? {
        val str = runBlocking {
            app.dataStore.data.map { settings -> settings[regionKey] }.first()
        }
        str ?: return null
        return Region.valueOf(str)
    }

    private fun setReg(region: Region?) {
        runBlocking {
            app.dataStore.edit { settings -> settings.setOrRemove(regionKey, region?.name) }
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

    private fun getProv(): Provider? {
        val str = runBlocking {
            app.dataStore.data.map { settings -> settings[providerKey] }.first()
        } ?: return null
        return Provider.valueOf(str)
    }

    private fun setProv(provider: Provider?) {
        runBlocking {
            app.dataStore.edit { settings -> settings.setOrRemove(providerKey, provider?.name) }
        }
        _provider = provider
    }
}