package org.alberto97.hisenseair.ayla.network.interceptors

import okhttp3.Request
import org.alberto97.hisenseair.ayla.network.interceptors.AuthorizationExtension.addAuthorization
import org.alberto97.hisenseair.network.OkHttpAppInterceptor
import org.alberto97.hisenseair.repositories.IAuthenticationRepository

class AuthInterceptor(private val repo: IAuthenticationRepository) : OkHttpAppInterceptor() {

    override fun interceptRequest(builder: Request.Builder, needsAuthorization: Boolean) {

        if (!needsAuthorization) return

        val token = repo.getToken()
        builder.addAuthorization(token)
    }
}