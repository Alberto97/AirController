package org.alberto97.aircontroller.common.models

data class AppDevice(
    val id: String,
    val name: String,
    val available: Boolean,
    val lanIp: String,
    val mac: String,
    val ssid: String?
)