package org.alberto97.hisenseair.provider.ayla.internal.network.interceptors

import okhttp3.Request

internal object AuthorizationExtension {
    fun Request.Builder.addAuthorization(token: String): Request.Builder {
        return this.header("Authorization", "auth_token $token")
    }
}