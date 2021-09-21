package org.alberto97.hisenseair.provider.ayla.internal.models

import com.google.gson.annotations.SerializedName

class WifiStatus(
    @SerializedName("connect_history")
    val connectHistory: Array<HistoryItem>,
    val dsn: String,
    @SerializedName("device_service")
    val deviceService: String,
    val mac: String,
    val mtime: Long,
    @SerializedName("host_symname")
    val hostSymname: String,
    @SerializedName("connected_ssid")
    val connectedSsid: String,
    val ant: Int,
    val wps: String,
    val rssi: Int,
    val bars: Int,
) {
    class Wrapper(@SerializedName("wifi_status") val wifiStatus: WifiStatus)
}

class HistoryItem(
    val ssid_info: String,
    val ssid_len: Int,
    val bssid: String,
    val error: Int,
    val msg: String,
    val mtime: Long,
    val last: Int,
    @SerializedName("ip_addr")
    val ipAddr: String,
    val netmask: String,
    @SerializedName("default_route")
    val defaultRoute: String,
    @SerializedName("dns_servers")
    val dnsServers: Array<String>
)

class WifiScanResults(
    val mtime: Long,
    val results: List<WifiScanResult>) {

    class WifiScanResult(
        val ssid: String,
        val type: String,
        val chan: Int,
        val signal: Int,
        val bars: Int,
        val security: String,
        val bssid: String
    )

    class Wrapper (
        @SerializedName("wifi_scan")
        val wifiScan: WifiScanResults
    )
}