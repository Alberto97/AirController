package org.alberto97.hisenseair.ayla.network.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.alberto97.hisenseair.ayla.network.interceptors.AuthorizationExtension.addAuthorization
import org.alberto97.hisenseair.repositories.IAuthenticationRepository

class TokenAuthenticator(private val repo: IAuthenticationRepository) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val successful = repo.refreshToken()
            if (successful) {
                val token = repo.getToken()

                // New request with new token
                response.request().newBuilder()
                    .addAuthorization(token)
                    .build()
            } else {
                null
            }
        }
    }
}