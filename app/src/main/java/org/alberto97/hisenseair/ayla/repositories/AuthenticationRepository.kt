package org.alberto97.hisenseair.ayla.repositories

import org.alberto97.hisenseair.ayla.models.Login
import org.alberto97.hisenseair.ayla.models.LoginOutput
import org.alberto97.hisenseair.ayla.models.LoginRefresh
import org.alberto97.hisenseair.ayla.models.UserRefresh
import org.alberto97.hisenseair.ayla.network.api.AylaLogin
import org.alberto97.hisenseair.repositories.IAuthenticationRepository
import retrofit2.HttpException

class AuthenticationRepository(
    private val service: AylaLogin,
    private val repository: ISecretsRepository
) :
    IAuthenticationRepository {

    override fun getToken(): String {
        return repository.authToken
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
    }
}