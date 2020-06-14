@file:Suppress("unused")

package org.alberto97.hisenseair.models

import com.google.gson.annotations.SerializedName

class Property (
    val type: String,
    val name: String,

    @SerializedName("base_type")
    val baseType: String,

    @SerializedName("read_only")
    val readOnly: Boolean,

    val direction: String,
    val scope: String,

    @SerializedName("data_updated_at")
    val dataUpdatedAt: String,

    val key: Int,

    @SerializedName("device_key")
    val deviceKey: Int,

    @SerializedName("product_name")
    val productName: String,

    @SerializedName("track_only_changes")
    val trackOnlyChanges: Boolean,

    @SerializedName("display_name")
    val displayName: String,

    @SerializedName("host_sw_version")
    val hostSwVersion: Boolean,

    @SerializedName("time_series")
    val timeSeries: Boolean,

    val derived: Boolean,

//    "app_type": null,
//    "recipe": null,
//    "value": null,
//    "denied_roles": [],
    val value: Any?,

    @SerializedName("ack_enabled")
    val ackEnabled: Boolean,

    @SerializedName("retention_days")
    val retentionDays: Int
)

class PropertyWrapper(
    val property: Property
)
