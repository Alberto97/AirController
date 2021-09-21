package org.alberto97.hisenseair.provider.ayla.internal.models

import com.google.gson.annotations.SerializedName

class ProductNameWrapper(
    val device: ProductName
)

class ProductName(
    @SerializedName("product_name")
    val productName: String
)