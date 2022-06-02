@file:Suppress("unused")

package org.alberto97.aircontroller.provider.ayla.internal.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
internal class ProductName(val device: Device) {
    constructor(productName: String) : this(Device(productName))

    @JsonClass(generateAdapter = true)
    class Device(
        @Json(name = "product_name")
        val productName: String
    )
}