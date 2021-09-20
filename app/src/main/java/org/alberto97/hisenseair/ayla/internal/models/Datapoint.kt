@file:Suppress("unused")

package org.alberto97.hisenseair.ayla.internal.models

import com.google.gson.annotations.SerializedName

class Datapoint(
    val value: Int
)

class DatapointWrapper(
    val datapoint: Datapoint
)

class DatapointOutput(
    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("created_at")
    val createdAt: String,
    val echo: Boolean,
    val value: Int
    //"metadata": {},
)

class DatapointOutputWrapper(
    val datapoint: DatapointOutput
) : Response()