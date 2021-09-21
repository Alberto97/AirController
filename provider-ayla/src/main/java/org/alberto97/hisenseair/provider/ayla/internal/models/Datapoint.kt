@file:Suppress("unused")

package org.alberto97.hisenseair.provider.ayla.internal.models

import com.google.gson.annotations.SerializedName

internal class Datapoint(value: Int) {
    val datapoint = Data(value)
    class Data(val value: Int)
}


internal class DatapointOutput(
    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("created_at")
    val createdAt: String,
    val echo: Boolean,
    val value: Int
    //"metadata": {},
) {
    class Wrapper(
        val datapoint: DatapointOutput
    )
}

