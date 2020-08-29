package org.alberto97.hisenseair.repositories

interface IAuthenticationRepository {
    fun getToken(): String
    suspend fun login(email: String, password: String): Boolean
    suspend fun refreshToken(): Boolean
}