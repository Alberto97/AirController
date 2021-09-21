@file:Suppress("unused")

package org.alberto97.hisenseair.provider.ayla.internal.models

import com.google.gson.annotations.SerializedName

internal class Datapoint(
    val value: Int
)

internal class DatapointWrapper(
    val datapoint: Datapoint
)

internal class DatapointOutput(
    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("created_at")
    val createdAt: String,
    val echo: Boolean,
    val value: Int
    //"metadata": {},
)

internal class DatapointOutputWrapper(
    val datapoint: DatapointOutput
)