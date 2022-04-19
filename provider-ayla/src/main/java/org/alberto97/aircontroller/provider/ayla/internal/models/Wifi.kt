@file:Suppress("unused")

package org.alberto97.aircontroller.provider.ayla.internal.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class WifiStatus(
    @Json(name = "connect_history")
    val connectHistory: Array<HistoryItem>,
    val dsn: String,
    @Json(name = "device_service")
    val deviceService: String,
    val mac: String,
    val mtime: Long,
    @Json(name = "host_symname")
    val hostSymname: String,
    @Json(name = "connected_ssid")
    val connectedSsid: String,
    val ant: Int,
    val wps: String,
    val rssi: Int,
    val bars: Int,
) {
    @JsonClass(generateAdapter = true)
    class Wrapper(@Json(name = "wifi_status") val wifiStatus: WifiStatus)
}

@JsonClass(generateAdapter = true)
internal class HistoryItem(
    val ssid_info: String,
    val ssid_len: Int,
    val bssid: String,
    val error: Int,
    val msg: String,
    val mtime: Long,
    val last: Int,
    @Json(name = "ip_addr")
    val ipAddr: String,
    val netmask: String,
    @Json(name = "default_route")
    val defaultRoute: String,
    @Json(name = "dns_servers")
    val dnsServers: Array<String>
)

@JsonClass(generateAdapter = true)
internal class WifiScanResults(
    val mtime: Long,
    val results: List<WifiScanResult>
) {

    @JsonClass(generateAdapter = true)
    class WifiScanResult(
        val ssid: String,
        val type: String,
        val chan: Int,
        val signal: Int,
        val bars: Int,
        val security: String,
        val bssid: String
    )

    @JsonClass(generateAdapter = true)
    class Wrapper(
        @Json(name = "wifi_scan")
        val wifiScan: WifiScanResults
    )
}