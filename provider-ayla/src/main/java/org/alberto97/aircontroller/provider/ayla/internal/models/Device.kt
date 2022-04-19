@file:Suppress("unused")

package org.alberto97.aircontroller.provider.ayla.internal.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
internal class Device(
    @Json(name = "product_name")
    val productName: String,
    val model: String,
    val dsn: String,

    @Json(name = "oem_model")
    val oemModel: String,

    @Json(name = "sw_version")
    val swVersion: String,

    @Json(name = "template_id")
    val templateId: Int,
    val mac: String,

    @Json(name = "unique_hardware_id")
    val uniqueHardwareId: String?,
    val hwsig: String,

    @Json(name = "lan_ip")
    val lanIp: String,

    @Json(name = "connected_at")
    val connectedAt: String,
    val key: Int,

    @Json(name = "lan_enabled")
    val lanEnabled: Boolean,

    @Json(name = "has_properties")
    val hasProperties: Boolean,

    @Json(name = "product_class")
    val productClass: String?,

    @Json(name = "connection_status")
    val connectionStatus: String,

    val lat: String,
    val lng: String,
    val locality: String,

    @Json(name = "device_type")
    val deviceType: String,

    val ssid: String?
) {
    @JsonClass(generateAdapter = true)
    class Wrapper(
        val device: Device
    )
}