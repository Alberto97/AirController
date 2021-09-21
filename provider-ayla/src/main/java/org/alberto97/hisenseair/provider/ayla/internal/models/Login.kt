@file:Suppress("unused")

package org.alberto97.hisenseair.provider.ayla.internal.models

import com.google.gson.annotations.SerializedName

// First access
internal class Login(email: String, password: String, app: Application) {
    val user = User(email, password, app)
    class User(val email: String, val password: String, val application: Application)
}

internal class Application(
    @SerializedName("app_id")
    val appId: String,

    @SerializedName("app_secret")
    val appSecret: String
)

// Refresh Token
internal class LoginRefresh(val user: UserRefresh)

internal class UserRefresh(
    @SerializedName("refresh_token")
    val refreshToken: String
)

// Response
internal class LoginOutput(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("expires_in")
    val expiresIn: Int,

    val role: String,

    @SerializedName("role_tags")
    val roleTags: List<String>
)