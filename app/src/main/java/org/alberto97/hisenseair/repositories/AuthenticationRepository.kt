package org.alberto97.hisenseair.repositories

import android.content.Context
import androidx.preference.PreferenceManager
import org.alberto97.hisenseair.AylaLogin
import org.alberto97.hisenseair.ayla.models.Login
import org.alberto97.hisenseair.ayla.models.LoginOutput
import org.alberto97.hisenseair.ayla.models.LoginRefresh
import org.alberto97.hisenseair.ayla.models.UserRefresh
import retrofit2.HttpException

interface IAuthenticationRepository {
    fun getToken(): String
    suspend fun login(email: String, password: String): Boolean
    suspend fun refreshToken(): Boolean
}

object AuthPrefs {
    const val SHARED_PREF_TOKEN_ACCESS = "access_token"
    const val SHARED_PREF_TOKEN_REFRESH = "refresh_token"
}

class AuthenticationRepository(private val service: AylaLogin, context: Context) : IAuthenticationRepository {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private var authToken = preferences.getString(AuthPrefs.SHARED_PREF_TOKEN_ACCESS, "")!!
    private var refreshToken = preferences.getString(AuthPrefs.SHARED_PREF_TOKEN_REFRESH, "")!!

    override fun getToken(): String {
        return authToken
    }

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            val login = Login(email, password)
            val output = service.login(login)

            saveAuthData(output)
            true
        } catch (e: HttpException) {
            false
        }
    }

    override suspend fun refreshToken(): Boolean {
        if (refreshToken.isEmpty())
            return false

        return try {
            val user = UserRefresh(refreshToken)
            val data = LoginRefresh(user)

            val resultBody = service.refreshToken(data)
            val successful = resultBody.error.isNullOrEmpty()
            if (successful)
                saveAuthData(resultBody)

            successful
        } catch (e: HttpException) {
            false
        }
    }

    private fun saveAuthData(output: LoginOutput) {
        authToken = output.accessToken
        refreshToken = output.refreshToken

        preferences.edit().apply {
            putString(AuthPrefs.SHARED_PREF_TOKEN_ACCESS, output.accessToken)
            putString(AuthPrefs.SHARED_PREF_TOKEN_REFRESH, output.refreshToken)
            apply()
        }
    }
}