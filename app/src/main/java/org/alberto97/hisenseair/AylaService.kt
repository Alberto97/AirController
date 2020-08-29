package org.alberto97.hisenseair

import org.alberto97.hisenseair.ayla.models.*
import retrofit2.http.*

interface AylaService {

    @Authorized
    @GET("devices.json")
    suspend fun getDevices(): List<DeviceWrapper>

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