@file:Suppress("unused")

package org.alberto97.aircontroller.provider.ayla.internal.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.alberto97.aircontroller.provider.ayla.internal.AylaType
import org.alberto97.aircontroller.provider.ayla.internal.models.adapters.AylaBoolean

internal open class Datapoint(
    // Required for moshi to be able to use a single object type
    // for the retrofit call while still taking advantage of the adapters
    @Transient
    val type: String
)

@JsonClass(generateAdapter = true)
internal class IntDatapoint(val datapoint: Data) : Datapoint(AylaType.INTEGER) {
    constructor(value: Int) : this(Data(value))

    @JsonClass(generateAdapter = true)
    class Data(val value: Int)
}

@JsonClass(generateAdapter = true)
internal class BooleanDatapoint(val datapoint: Data) : Datapoint(AylaType.INTEGER) {
    constructor(value: Boolean) : this(Data(value))

    @JsonClass(generateAdapter = true)
    class Data(@AylaBoolean val value: Boolean?)
}


@JsonClass(generateAdapter = true)
internal class DatapointOutput(
    @Json(name = "updated_at")
    val updatedAt: String,

    @Json(name = "created_at")
    val createdAt: String,
    val echo: Boolean,
    val value: Int
    //"metadata": {},
) {
    @JsonClass(generateAdapter = true)
    class Wrapper(
        val datapoint: DatapointOutput
    )
}

