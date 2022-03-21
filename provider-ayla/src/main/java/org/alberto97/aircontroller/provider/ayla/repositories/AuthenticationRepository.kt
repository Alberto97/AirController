package org.alberto97.aircontroller.provider.ayla.repositories

import android.util.Log
import org.alberto97.aircontroller.common.enums.Region
import org.alberto97.aircontroller.common.models.DefaultErrors
import org.alberto97.aircontroller.common.models.ResultWrapper
import org.alberto97.aircontroller.common.repositories.ISettingsRepository
import org.alberto97.aircontroller.provider.ayla.internal.AylaExtensions.aylaError
import org.alberto97.aircontroller.provider.ayla.internal.models.*
import org.alberto97.aircontroller.provider.ayla.internal.network.api.AylaLogin
import org.alberto97.aircontroller.provider.ayla.internal.repositories.ISecretsRepository
import org.alberto97.aircontroller.provider.repositories.AuthErrorCodes
import org.alberto97.aircontroller.provider.repositories.IAuthenticationRepository
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

internal class AuthenticationRepository(
    private val service: AylaLogin,
    private val repository: ISecretsRepository,
    private val settings: ISettingsRepository
) : IAuthenticationRepository {
    companion object {
        const val LOG_TAG = "AuthRepository"
    }

    private val euApplication = Application(
        appId = "a-Hisense-oem-eu-field-id",
        appSecret = "a-Hisense-oem-eu-field-YR569cQPGH7l67Gf5PUmQv4jiMs-Bk8secEGY52ZSsJ4rOuCk-UNiWQ"
    )

    private val usApplication = Application(
        appId = "a-Hisense-oem-us-field-id",
        appSecret = "a-Hisense-oem-us-field-HWdBUFTRqeziogEZwANYE2r8tZE"
    )

    private val appMap = mapOf(
        Region.EU to euApplication,
        Region.US to usApplication
    )

    override fun getToken(): String {
        return repository.authToken
    }

    override suspend fun login(email: String, password: String): ResultWrapper<Unit> {
        val region = settings.region
        val app =
            appMap[region] ?: return ResultWrapper.Error("App id and secret not found for $region")

        try {
            val login = Login(email, password, app)
            val output = service.login(login)

            saveAuthData(output)
            return ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> when (e.code()) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> return handleHttpUnauthorized(e.aylaError())
                    HttpURLConnection.HTTP_NOT_FOUND -> handleHttpNotFound(app)
                }
                is IOException -> return DefaultErrors.connectivityError()
            }
            Log.e("HiLogin", e.stackTraceToString())
            return ResultWrapper.Error("Authentication failed")
        }
    }

    override suspend fun refreshToken(): ResultWrapper<Unit> {
        val refreshToken = repository.refreshToken
        if (refreshToken.isEmpty())
            return ResultWrapper.Error("Missing refresh token", AuthErrorCodes.UNAUTHORIZED)

        try {
            val data = LoginRefresh(refreshToken)

            val resultBody = service.refreshToken(data)
            saveAuthData(resultBody)

            return ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> when (e.code()) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> return handleHttpUnauthorized(e.aylaError())
                }
                is IOException -> return DefaultErrors.connectivityError()
            }
            Log.e("HiLogin", e.stackTraceToString())
            return ResultWrapper.Error("Authentication failed")
        }
    }

    private fun handleHttpUnauthorized(msg: String?): ResultWrapper<Unit> {
        settings.loggedIn = false
        val message = msg ?: "Authentication failed"
        return ResultWrapper.Error(message, AuthErrorCodes.UNAUTHORIZED)
    }

    private fun handleHttpNotFound(app: Application): ResultWrapper<Unit> {
        Log.i(LOG_TAG, "app_id: ${app.appId}")
        Log.i(LOG_TAG, "app_secret: ${app.appSecret}")
        throw IllegalArgumentException("The app_id or app_secret are invalid")
    }

    override fun logout() {
        repository.authToken = ""
        repository.refreshToken = ""
        settings.loggedIn = false
    }

    private fun saveAuthData(output: LoginOutput) {
        repository.authToken = output.accessToken
        repository.refreshToken = output.refreshToken
        settings.loggedIn = true
    }
}