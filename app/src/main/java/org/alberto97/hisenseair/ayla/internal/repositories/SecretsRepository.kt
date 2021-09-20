package org.alberto97.hisenseair.ayla.internal.repositories

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


object AuthPrefs {
    const val SHARED_PREF_TOKEN_ACCESS = "access_token"
    const val SHARED_PREF_TOKEN_REFRESH = "refresh_token"
}

interface ISecretsRepository {
    var authToken: String
    var refreshToken: String
}

class SecretsRepository(app: Application) : ISecretsRepository {

    companion object {
        const val SECRETS_FILE_NAME = "secrets"
    }

    private val masterKey = MasterKey.Builder(app, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val preferences: SharedPreferences = EncryptedSharedPreferences.create(
        app,
        SECRETS_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

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