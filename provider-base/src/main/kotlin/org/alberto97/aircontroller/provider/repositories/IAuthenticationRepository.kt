package org.alberto97.aircontroller.provider.repositories

import org.alberto97.aircontroller.common.models.ResultWrapper


object AuthErrorCodes {
    const val UNAUTHORIZED = 1
}

interface IAuthenticationRepository {
    fun getToken(): String
    suspend fun login(email: String, password: String): ResultWrapper<Unit>
    suspend fun refreshToken(): ResultWrapper<Unit>
    fun logout()
}