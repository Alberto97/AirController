package org.alberto97.hisenseair.ayla.internal.network.interceptors

import okhttp3.Request
import org.alberto97.hisenseair.ayla.internal.network.interceptors.AuthorizationExtension.addAuthorization
import org.alberto97.hisenseair.network.AuthorizedRequestInterceptor
import org.alberto97.hisenseair.repositories.IAuthenticationRepository

class AuthInterceptor(private val repo: IAuthenticationRepository) : AuthorizedRequestInterceptor() {

    override fun authorize(request: Request): Request {
        val token = repo.getToken()

        val builder = request.newBuilder().apply {
            addAuthorization(token)
        }

        return builder.build()
    }
}