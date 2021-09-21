package org.alberto97.hisenseair.provider.demo.repositories

import org.alberto97.hisenseair.common.models.ResultWrapper
import org.alberto97.hisenseair.provider.repositories.IAuthenticationRepository
import org.alberto97.hisenseair.common.repositories.ISettingsRepository

internal class AuthenticationRepository(
    private val settings: ISettingsRepository
): IAuthenticationRepository {

    override fun getToken(): String {
        return ""
    }

    override suspend fun login(email: String, password: String): ResultWrapper<Unit> {
        settings.loggedIn = true
        return ResultWrapper.Success(Unit)
    }

    override suspend fun refreshToken(): ResultWrapper<Unit> {
        settings.loggedIn = true
        return ResultWrapper.Success(Unit)
    }

    override fun logout() {
        settings.loggedIn = false
    }
}