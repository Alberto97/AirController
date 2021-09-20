@file:Suppress("unused")

package org.alberto97.hisenseair.ayla.internal.models

import com.google.gson.annotations.SerializedName

class DeviceWrapper(
    val device: Device
)

class Device(
    @SerializedName("product_name")
    val productName: String,
    val model: String,
    val dsn: String,

    @SerializedName("oem_model")
    val oemModel: String,

    @SerializedName("sw_version")
    val swVersion: String,

    @SerializedName("template_id")
    val templateId: Int,
    val mac: String,

    @SerializedName("unique_hardware_id")
    val uniqueHardwareId: String,
    val hwsig: String,

    @SerializedName("lan_ip")
    val lanIp: String,

    @SerializedName("connected_at")
    val connectedAt: String,
    val key: Int,

    @SerializedName("lan_enabled")
    val lanEnabled: Boolean,

    @SerializedName("has_properties")
    val hasProperties: Boolean,

    @SerializedName("product_class")
    val productClass: String,

    @SerializedName("connection_status")
    val connectionStatus: String,

    val lat: String,
    val lng: String,
    val locality: String,

    @SerializedName("device_type")
    val deviceType: String,

    val ssid: String
)