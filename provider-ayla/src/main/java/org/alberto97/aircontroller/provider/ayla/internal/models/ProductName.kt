@file:Suppress("unused")

package org.alberto97.aircontroller.provider.ayla.internal.models

import com.google.gson.annotations.SerializedName


internal class ProductName(productName: String) {
    val device = Device(productName)

    class Device(
        @SerializedName("product_name")
        val productName: String
    )
}