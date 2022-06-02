@file:Suppress("unused")

package org.alberto97.aircontroller.provider.ayla.internal.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// First access
@JsonClass(generateAdapter = true)
internal class Login(val user: User) {
    constructor(email: String, password: String, app: Application) : this(User(email, password, app))

    @JsonClass(generateAdapter = true)
    class User(val email: String, val password: String, val application: Application)
}

@JsonClass(generateAdapter = true)
internal class Application(
    @Json(name = "app_id")
    val appId: String,

    @Json(name = "app_secret")
    val appSecret: String
)

// Refresh Token
@JsonClass(generateAdapter = true)
internal class LoginRefresh(val user: UserRefresh) {
    constructor(refreshToken: String) : this(UserRefresh(refreshToken))

    @JsonClass(generateAdapter = true)
    class UserRefresh(
        @Json(name = "refresh_token")
        val refreshToken: String
    )
}

// Response
@JsonClass(generateAdapter = true)
internal class LoginOutput(
    @Json(name = "access_token")
    val accessToken: String,

    @Json(name = "refresh_token")
    val refreshToken: String,

    @Json(name = "expires_in")
    val expiresIn: Int,

    val role: String,

    @Json(name = "role_tags")
    val roleTags: List<String>
)