package org.alberto97.aircontroller.provider.ayla.internal.network.api

import org.alberto97.aircontroller.provider.ayla.internal.models.Login
import org.alberto97.aircontroller.provider.ayla.internal.models.LoginOutput
import org.alberto97.aircontroller.provider.ayla.internal.models.LoginRefresh
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AylaLogin {

    /**
     * Possible HTTP response codes:
     * 200: OK (If user successfully logged in)
     * 401: Unauthorized (If app_id and app_secret do not match OR if email and password do not match, etc.)
     * 404: The app_id or app_secret are invalid.
     */
    @POST("sign_in.json")
    suspend fun login(@Body login: Login): LoginOutput

    /**
     * Possible HTTP response codes:
     * 200: OK (If new access token was successfully generated)
     * 401: Unauthorized (If refresh token does not match current user)
     */
    @POST("refresh_token.json")
    suspend fun refreshToken(@Body login: LoginRefresh): LoginOutput
}