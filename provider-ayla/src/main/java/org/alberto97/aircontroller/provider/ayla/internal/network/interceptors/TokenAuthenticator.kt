package org.alberto97.aircontroller.provider.ayla.internal.network.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.alberto97.aircontroller.common.models.ResultWrapper
import org.alberto97.aircontroller.provider.ayla.internal.network.interceptors.AuthorizationExtension.addAuthorization
import org.alberto97.aircontroller.provider.repositories.IAuthenticationRepository

internal class TokenAuthenticator(private val repo: IAuthenticationRepository) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val resp = repo.refreshToken()
            if (resp is ResultWrapper.Success) {
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