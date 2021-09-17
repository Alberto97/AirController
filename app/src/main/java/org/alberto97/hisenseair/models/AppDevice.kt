package org.alberto97.hisenseair.models

data class AppDevice(
    val id: String,
    val name: String,
    val available: Boolean,
    val lanIp: String,
    val mac: String,
    val ssid: String?
)