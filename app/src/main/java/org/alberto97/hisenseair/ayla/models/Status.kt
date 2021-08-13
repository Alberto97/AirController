package org.alberto97.hisenseair.ayla.models

import com.google.gson.annotations.SerializedName

class Status (
    @SerializedName("DSN")
    val dsn: String,

    @SerializedName("dsn")
    val DSN: String,

    val model: String,

    @SerializedName("api_version")
    val apiVersion: String,

    @SerializedName("device_service")
    val deviceService: String,

    val mac: String,

    @SerializedName("last_connect_mtime")
    val lastConnectMtime: Long,

    val mtime: Long,
    val version: String,
    val build: String,
    val features: List<String>
)