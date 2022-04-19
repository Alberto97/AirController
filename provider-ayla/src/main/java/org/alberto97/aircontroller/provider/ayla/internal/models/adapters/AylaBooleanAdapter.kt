package org.alberto97.aircontroller.provider.ayla.internal.models.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class AylaBoolean

object AylaBooleanAdapter {
    @FromJson
    @AylaBoolean
    fun fromJson(value: Int?): Boolean? {
        val data = value ?: return null
        return data > 0
    }

    @ToJson
    fun toJson(@AylaBoolean value: Boolean?): Int? {
        val data = value ?: return null
        return if (data) 1 else 0
    }
}
