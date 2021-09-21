package org.alberto97.hisenseair.provider.ayla.internal.network.api

import org.alberto97.hisenseair.common.network.Authorized
import org.alberto97.hisenseair.provider.ayla.internal.models.*
import retrofit2.Response
import retrofit2.http.*

interface AylaService {

    @Authorized
    @GET("devices.json")
    suspend fun getDevices(): List<DeviceWrapper>

    @Authorized
    @POST("devices.json")
    suspend fun postDevice(@Body device: SetupDevice.Wrapper): DeviceWrapper

    @Authorized
    @GET("devices/connected.json")
    suspend fun connected(
        @Query("dsn")
        dsn: String,
        @Query("setup_token")
        setupToken: String?
    ): ConnectedDevice.Wrapper

    @Authorized
    @DELETE("devices/{key}.json")
    suspend fun deleteDevice(
        @Path("key")
        key: Int
    ): Response<Unit>

    @Authorized
    @GET("dsns/{dsn}.json")
    suspend fun getDevice(
        @Path("dsn")
        dsn: String
    ): DeviceWrapper

    @Authorized
    @PUT("dsns/{dsn}.json")
    suspend fun putDevice(
        @Path("dsn")
        dsn: String,

        @Body
        deviceObj: ProductNameWrapper
    )

    @Authorized
    @GET("dsns/{dsn}/properties.json")
    suspend fun getDeviceProperties(
        @Path("dsn")
        dsn: String
    ): List<PropertyWrapper>

    @Authorized
    @GET("dsns/{dsn}/properties/{property}.json")
    suspend fun getDeviceProperty(
        @Path("dsn")
        dsn: String,

        @Path("property")
        property: String
    ): PropertyWrapper

    @Authorized
    @POST("dsns/{dsn}/properties/{property}/datapoints.json")
    suspend fun setDeviceProperty(
        @Path("dsn")
        dsn: String,

        @Path("property")
        property: String,

        @Body
        datapoint: DatapointWrapper
    ): DatapointOutput
}