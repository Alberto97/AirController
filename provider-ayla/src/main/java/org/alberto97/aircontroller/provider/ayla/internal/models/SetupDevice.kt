@file:Suppress("unused")

package org.alberto97.aircontroller.provider.ayla.internal.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class SetupDevice(val device: Device) {
    constructor(dsn: String, setupToken: String?, regToken: String? = null) : this(Device(dsn, setupToken, regToken))

    @JsonClass(generateAdapter = true)
    class Device(
        val dsn: String,
        @Json(name = "setup_token") val setupToken: String?,
        @Json(name = "regtoken") val regToken: String? = null
    )
}

@JsonClass(generateAdapter = true)
internal class ConnectedDevice(
    @Json(name = "connected_at")
    val connectedAt: String,
    @Json(name = "device_type")
    val deviceType: String,
    @Json(name = "lan_ip")
    val lanIp: String?,
    @Json(name = "registration_type")
    val registrationType: String
) {
    @JsonClass(generateAdapter = true)
    class Wrapper(
        val device: ConnectedDevice
    )
}