@file:Suppress("unused")

package org.alberto97.aircontroller.provider.ayla.internal.models

import com.google.gson.annotations.SerializedName

internal class SetupDevice(dsn: String, setupToken: String?, regToken: String? = null) {
    val device = Device(dsn, setupToken, regToken)
    class Device(
        val dsn: String,
        @SerializedName("setup_token") val setupToken: String?,
        @SerializedName("regtoken") val regToken: String? = null
    )
}

internal class ConnectedDevice(
    @SerializedName("connected_at")
    val connectedAt: String,
    @SerializedName("device_type")
    val deviceType: String,
    @SerializedName("lan_ip")
    val lanIp: String?,
    @SerializedName("registration_type")
    val registrationType: String
) {
    class Wrapper(
        val device: ConnectedDevice
    )
}