package org.alberto97.aircontroller.provider.ayla

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import org.alberto97.aircontroller.provider.ayla.internal.models.*
import org.alberto97.aircontroller.provider.ayla.serialization.MoshiAylaExtensions.addAylaBooleanAdapter
import org.alberto97.aircontroller.provider.ayla.serialization.MoshiAylaExtensions.addAylaDatapointAdapter
import org.alberto97.aircontroller.provider.ayla.serialization.MoshiAylaExtensions.addAylaPropertyAdapter
import org.junit.Assert.assertEquals
import org.junit.Test


@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalStdlibApi::class)
class SerializationTest {

    private val moshi = Moshi.Builder()
        .addAylaBooleanAdapter()
        .addAylaPropertyAdapter()
        .addAylaDatapointAdapter()
        .build()

    @Test
    fun testDeserializeWrappedList() {
        val json = javaClass.classLoader?.getResource("properties.json")!!.readText()

        val adapter = moshi.adapter<List<Property.Wrapper>>()
        val list = adapter.fromJson(json)

        assertEquals(49, list?.size)
    }

    @Test
    fun testSerializeBoolDatapoint() {
        val datapoint = BooleanDatapoint(true)

        val adapter = moshi.adapter<BooleanDatapoint>()
        val result = adapter.toJson(datapoint)
        println(result)

        assertEquals("{\"datapoint\":{\"value\":1}}", result)
    }

    @Test
    fun testSerializeIntDatapoint() {
        val datapoint = IntDatapoint(0)

        val adapter = moshi.adapter<IntDatapoint>()
        val result = adapter.toJson(datapoint)
        println(result)

        assertEquals("{\"datapoint\":{\"value\":0}}", result)
    }

    @Test
    fun nestedClassTest() {
        val productName = ProductName("DeviceName")

        val adapter = moshi.adapter<ProductName>()
        val result = adapter.toJson(productName)
        println(result)

        assertEquals("{\"device\":{\"product_name\":\"DeviceName\"}}", result)
    }
}
