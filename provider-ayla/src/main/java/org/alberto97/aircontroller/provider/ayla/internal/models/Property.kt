@file:Suppress("unused")

package org.alberto97.aircontroller.provider.ayla.internal.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.alberto97.aircontroller.provider.ayla.internal.models.adapters.AylaBoolean

@JsonClass(generateAdapter = true)
open class Property(
    val type: String,
    val name: String,

    @property:Json(name = "base_type")
    val baseType: String,

    @property:Json(name = "read_only")
    val readOnly: Boolean,

    val direction: String,
    val scope: String,

    @property:Json(name = "data_updated_at")
    val dataUpdatedAt: String,

    val key: Int,

    @property:Json(name = "device_key")
    val deviceKey: Int,

    @property:Json(name = "product_name")
    val productName: String,

    @property:Json(name = "track_only_changes")
    val trackOnlyChanges: Boolean,

    @property:Json(name = "display_name")
    val displayName: String,

    @property:Json(name = "host_sw_version")
    val hostSwVersion: Boolean,

    @property:Json(name = "time_series")
    val timeSeries: Boolean,

    val derived: Boolean,

//    "app_type": null,
//    "recipe": null,
//    "value": null,
//    "denied_roles": [],
    open val value: Any?,

    @property:Json(name = "ack_enabled")
    val ackEnabled: Boolean,

    @property:Json(name = "retention_days")
    val retentionDays: Int?
) {
    @JsonClass(generateAdapter = true)
    class Wrapper(
        val property: Property
    )
}

@JsonClass(generateAdapter = true)
class BooleanProperty(
    @AylaBoolean
    override val value: Boolean?,
    type: String,
    name: String,
    baseType: String,
    readOnly: Boolean,
    direction: String,
    scope: String,
    dataUpdatedAt: String,
    key: Int,
    deviceKey: Int,
    productName: String,
    trackOnlyChanges: Boolean,
    displayName: String,
    hostSwVersion: Boolean,
    timeSeries: Boolean,
    derived: Boolean,
    ackEnabled: Boolean,
    retentionDays: Int?
) : Property(
    type,
    name,
    baseType,
    readOnly,
    direction,
    scope,
    dataUpdatedAt,
    key,
    deviceKey,
    productName,
    trackOnlyChanges,
    displayName,
    hostSwVersion,
    timeSeries,
    derived,
    value,
    ackEnabled,
    retentionDays
)


@JsonClass(generateAdapter = true)
class IntProperty(
    override val value: Int?,
    type: String,
    name: String,
    baseType: String,
    readOnly: Boolean,
    direction: String,
    scope: String,
    dataUpdatedAt: String,
    key: Int,
    deviceKey: Int,
    productName: String,
    trackOnlyChanges: Boolean,
    displayName: String,
    hostSwVersion: Boolean,
    timeSeries: Boolean,
    derived: Boolean,
    ackEnabled: Boolean,
    retentionDays: Int?
) : Property(
    type,
    name,
    baseType,
    readOnly,
    direction,
    scope,
    dataUpdatedAt,
    key,
    deviceKey,
    productName,
    trackOnlyChanges,
    displayName,
    hostSwVersion,
    timeSeries,
    derived,
    value,
    ackEnabled,
    retentionDays
)

@JsonClass(generateAdapter = true)
class StringProperty(
    override val value: String?,
    type: String,
    name: String,
    baseType: String,
    readOnly: Boolean,
    direction: String,
    scope: String,
    dataUpdatedAt: String,
    key: Int,
    deviceKey: Int,
    productName: String,
    trackOnlyChanges: Boolean,
    displayName: String,
    hostSwVersion: Boolean,
    timeSeries: Boolean,
    derived: Boolean,
    ackEnabled: Boolean,
    retentionDays: Int?
) : Property(
    type,
    name,
    baseType,
    readOnly,
    direction,
    scope,
    dataUpdatedAt,
    key,
    deviceKey,
    productName,
    trackOnlyChanges,
    displayName,
    hostSwVersion,
    timeSeries,
    derived,
    value,
    ackEnabled,
    retentionDays
)


