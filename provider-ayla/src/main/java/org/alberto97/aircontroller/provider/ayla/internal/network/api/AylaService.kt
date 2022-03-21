package org.alberto97.aircontroller.provider.ayla.internal.network.api

import org.alberto97.aircontroller.common.network.Authorized
import org.alberto97.aircontroller.provider.ayla.internal.models.*
import retrofit2.Response
import retrofit2.http.*

internal interface AylaService {

    /**
     * Retrieve a list of all devices for the user, with some details.
     * Possible HTTP response codes:
     * 200: OK
     * 401: Unauthorized (If app_id and app_secret do not match OR if email and password do not match, etc.)
     */
    @Authorized
    @GET("devices.json")
    suspend fun getDevices(): List<Device.Wrapper>

    /**
     * Register a new device for the specified user.
     * Possible HTTP response codes:
     * 201: Created
     * 401: Unauthorized
     * 404: Unable to find registrable device with user ID
     * 417: User ID is nil or device registration candidate is invalid
     */
    @Authorized
    @POST("devices.json")
    suspend fun postDevice(@Body device: SetupDevice): Device.Wrapper

    @Authorized
    @GET("devices/connected.json")
    suspend fun connected(
        @Query("dsn")
        dsn: String,
        @Query("setup_token")
        setupToken: String?
    ): ConnectedDevice.Wrapper

    /**
     * Unregister a device from the current user.
     * Possible HTTP response codes:
     * 200: OK
     * 401: Unauthorized
     * 404: Not Found
     */
    @Authorized
    @DELETE("devices/{key}.json")
    suspend fun deleteDevice(
        @Path("key")
        key: Int
    ): Response<Unit>

    /**
     * Fetch device details specified by Device Serial Number (DSN).
     * Possible HTTP response codes:
     * 200: OK (if user successfully logged in)
     * 400: Invalid Parameters Supplied
     * 401: Unauthorized
     */
    @Authorized
    @GET("dsns/{dsn}.json")
    suspend fun getDevice(
        @Path("dsn")
        dsn: String
    ): Device.Wrapper

    /**
     * Update the display name of the device.
     * Possible HTTP response codes:
     * 200: OK (if user successfully logged in)
     * 401: Unauthorized
     * 403: Forbidden
     * 404: DSN with provided ID Not Found
     */
    @Authorized
    @PUT("dsns/{dsn}.json")
    suspend fun putDevice(
        @Path("dsn")
        dsn: String,

        @Body
        deviceObj: ProductName
    )

    /**
     * Retrieve all the properties for a specified device serial number (DSN).
     * Possible HTTP response codes:
     * 200: OK
     * 401: Unauthorized
     * 404: Not Found
     */
    @Authorized
    @GET("dsns/{dsn}/properties.json")
    suspend fun getDeviceProperties(
        @Path("dsn")
        dsn: String
    ): List<Property.Wrapper>

    /**
     * Retrieve details about a device property on a specific device.
     * Possible HTTP response codes:
     * 200: OK
     * 401: Unauthorized
     * 404: Not Found
     */
    @Authorized
    @GET("dsns/{dsn}/properties/{property}.json")
    suspend fun getDeviceProperty(
        @Path("dsn")
        dsn: String,

        @Path("property")
        property: String
    ): Property.Wrapper

    /**
     * Create one datapoint for a property on the specified device.
     * Possible HTTP response codes:
     * 201: Created
     * 401: Unauthorized
     * 403: Forbidden
     * 404: Not Found
     */
    @Authorized
    @POST("dsns/{dsn}/properties/{property}/datapoints.json")
    suspend fun setDeviceProperty(
        @Path("dsn")
        dsn: String,

        @Path("property")
        property: String,

        @Body
        datapoint: Datapoint
    ): DatapointOutput
}