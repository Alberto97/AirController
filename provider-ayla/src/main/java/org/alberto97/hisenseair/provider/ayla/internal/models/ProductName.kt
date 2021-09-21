package org.alberto97.hisenseair.provider.ayla.internal.models

import com.google.gson.annotations.SerializedName


internal class ProductName(
    @SerializedName("product_name")
    val productName: String
) {
    class Wrapper(
        val device: ProductName
    )
}