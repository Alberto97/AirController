package org.alberto97.hisenseair.ayla.models

import com.google.gson.annotations.SerializedName

class SetupDevice(
    val dsn: String,
    @SerializedName("setup_token") val setupToken: String?,
    @SerializedName("regtoken") val regToken: String? = null
) {
    class Wrapper(
        val device: SetupDevice
    )
}

class ConnectedDevice(
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