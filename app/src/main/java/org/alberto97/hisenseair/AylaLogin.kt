package org.alberto97.hisenseair

import org.alberto97.hisenseair.models.Login
import org.alberto97.hisenseair.models.LoginOutput
import org.alberto97.hisenseair.models.LoginRefresh
import retrofit2.http.Body
import retrofit2.http.POST

interface AylaLogin {

    @POST("sign_in.json")
    suspend fun login(@Body login: Login): LoginOutput

    @POST("refresh_token.json")
    suspend fun refreshToken(@Body login: LoginRefresh): LoginOutput
}