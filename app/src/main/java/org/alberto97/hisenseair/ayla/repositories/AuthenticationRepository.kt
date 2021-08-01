package org.alberto97.hisenseair.ayla.repositories

import org.alberto97.hisenseair.ayla.models.*
import org.alberto97.hisenseair.ayla.network.api.AylaLogin
import org.alberto97.hisenseair.repositories.IAuthenticationRepository
import org.alberto97.hisenseair.repositories.ISettingsRepository
import org.alberto97.hisenseair.repositories.Region
import retrofit2.HttpException

class AuthenticationRepository(
    private val service: AylaLogin,
    private val repository: ISecretsRepository,
    private val settings: ISettingsRepository
) : IAuthenticationRepository {

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

    override suspend fun login(email: String, password: String): Boolean {
        val app = appMap[settings.region] ?: return false

        return try {
            val login = Login(email, password, app)
            val output = service.login(login)

            saveAuthData(output)
            true
        } catch (e: HttpException) {
            false
        }
    }

    override suspend fun refreshToken(): Boolean {
        val refreshToken = repository.refreshToken
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
        repository.authToken = output.accessToken
        repository.refreshToken = output.refreshToken
        settings.loggedIn = true
    }
}