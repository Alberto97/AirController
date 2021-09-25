package org.alberto97.aircontroller.provider.ayla.internal.network.api

import org.alberto97.aircontroller.provider.ayla.internal.models.Status
import org.alberto97.aircontroller.provider.ayla.internal.models.WifiScanResults
import org.alberto97.aircontroller.provider.ayla.internal.models.WifiStatus
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

internal interface PairApi {

    @GET("status.json")
    suspend fun status(): Status

    @GET("wifi_status.json")
    suspend fun wifiStatus(): WifiStatus.Wrapper

    @POST("wifi_scan.json")
    suspend fun wifiScan(): Response<Unit>

    @GET("wifi_scan_results.json")
    suspend fun wifiScanResults(): WifiScanResults.Wrapper

    @POST("wifi_connect.json")
    suspend fun wifiConnect(
        @Query("ssid")
        ssid: String,
        @Query("key")
        password: String?,
        @Query("setup_token")
        setupToken: String?
    ): Response<Unit>

    @PUT("wifi_stop_ap.json")
    suspend fun stopAp(): Response<Unit>
}