package org.alberto97.aircontroller.provider.ayla.internal.repositories

import android.app.Application
import android.content.Context
import android.content.SharedPreferences


internal object AuthPrefs {
    const val SHARED_PREF_TOKEN_ACCESS = "access_token"
    const val SHARED_PREF_TOKEN_REFRESH = "refresh_token"
}

internal interface ISecretsRepository {
    var authToken: String
    var refreshToken: String
}

internal class SecretsRepository(app: Application) : ISecretsRepository {

    companion object {
        const val SECRETS_FILE_NAME = "secrets_prefs"
    }

    private val preferences: SharedPreferences =
        app.getSharedPreferences(SECRETS_FILE_NAME, Context.MODE_PRIVATE)

    private var _authToken = getAuth()
    private var _refreshToken = getRefresh()

    override var authToken: String
        get() = _authToken
        set(value) = setAuth(value)

    override var refreshToken: String
        get() = _refreshToken
        set(value) = setRefresh(value)

    private fun getAuth(): String {
        return preferences.getString(AuthPrefs.SHARED_PREF_TOKEN_ACCESS, "")!!
    }

    private fun setAuth(token: String) {
        preferences.edit().apply {
            putString(AuthPrefs.SHARED_PREF_TOKEN_ACCESS, token)
            apply()
        }
        _authToken = token
    }

    private fun getRefresh(): String {
        return preferences.getString(AuthPrefs.SHARED_PREF_TOKEN_REFRESH, "")!!
    }

    private fun setRefresh(token: String) {
        preferences.edit().apply {
            putString(AuthPrefs.SHARED_PREF_TOKEN_REFRESH, token)
            apply()
        }
        _refreshToken = token
    }
}