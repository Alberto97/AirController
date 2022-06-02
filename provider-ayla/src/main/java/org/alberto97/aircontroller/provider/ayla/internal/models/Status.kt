@file:Suppress("unused")

package org.alberto97.aircontroller.provider.ayla.internal.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class Status (
    @Json(name = "DSN")
    val dsn: String,

    @Json(name = "dsn")
    val DSN: String,

    val model: String,

    @Json(name = "api_version")
    val apiVersion: String,

    @Json(name = "device_service")
    val deviceService: String,

    val mac: String,

    @Json(name = "last_connect_mtime")
    val lastConnectMtime: Long,

    val mtime: Long,
    val version: String,
    val build: String,
    val features: List<String>
)