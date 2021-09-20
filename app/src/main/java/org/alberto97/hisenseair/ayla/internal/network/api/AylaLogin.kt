package org.alberto97.hisenseair.ayla.internal.network.api

import org.alberto97.hisenseair.ayla.internal.models.Login
import org.alberto97.hisenseair.ayla.internal.models.LoginOutput
import org.alberto97.hisenseair.ayla.internal.models.LoginRefresh
import retrofit2.http.Body
import retrofit2.http.POST

interface AylaLogin {

    @POST("sign_in.json")
    suspend fun login(@Body login: Login): LoginOutput

    @POST("refresh_token.json")
    suspend fun refreshToken(@Body login: LoginRefresh): LoginOutput
}