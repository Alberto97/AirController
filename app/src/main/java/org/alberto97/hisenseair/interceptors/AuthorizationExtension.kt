package org.alberto97.hisenseair.interceptors

import okhttp3.Request

object AuthorizationExtension {
    fun Request.Builder.addAuthorization(token: String): Request.Builder {
        return this.header("Authorization", "auth_token $token")
    }
}