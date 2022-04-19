package org.alberto97.aircontroller.provider.ayla.serialization

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import org.alberto97.aircontroller.provider.ayla.internal.AylaType
import org.alberto97.aircontroller.provider.ayla.internal.models.*
import org.alberto97.aircontroller.provider.ayla.internal.models.BooleanDatapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.BooleanProperty
import org.alberto97.aircontroller.provider.ayla.internal.models.Datapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.IntDatapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.Property
import org.alberto97.aircontroller.provider.ayla.internal.models.adapters.AylaBooleanAdapter

object MoshiAylaExtensions {
    fun Moshi.Builder.addAyla(): Moshi.Builder {
        return this
            .addAylaBooleanAdapter()
            .addAylaPropertyAdapter()
            .addAylaDatapointAdapter()
    }

    fun Moshi.Builder.addAylaBooleanAdapter(): Moshi.Builder {
        return this.add(AylaBooleanAdapter)
    }

    fun Moshi.Builder.addAylaDatapointAdapter(): Moshi.Builder {
        val adapter = PolymorphicJsonAdapterFactory.of(Datapoint::class.java, "type")
            .withSubtype(BooleanDatapoint::class.java, AylaType.BOOLEAN)
            .withSubtype(IntDatapoint::class.java, AylaType.INTEGER)
        return this.add(adapter)
    }

    fun Moshi.Builder.addAylaPropertyAdapter(): Moshi.Builder {
        val adapter = PolymorphicJsonAdapterFactory.of(Property::class.java, "base_type")
            .withSubtype(BooleanProperty::class.java, AylaType.BOOLEAN)
            .withSubtype(IntProperty::class.java, AylaType.DECIMAL)
            .withSubtype(IntProperty::class.java, AylaType.INTEGER)
            .withSubtype(StringProperty::class.java, AylaType.STRING)
        return this.add(adapter)
    }

}