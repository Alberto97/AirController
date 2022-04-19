package org.alberto97.aircontroller.provider.ayla.repositories

import kotlinx.coroutines.runBlocking
import org.alberto97.aircontroller.common.features.FanSpeed
import org.alberto97.aircontroller.common.features.SleepMode
import org.alberto97.aircontroller.common.features.TempType
import org.alberto97.aircontroller.common.features.WorkMode
import org.alberto97.aircontroller.common.models.ResultWrapper
import org.alberto97.aircontroller.provider.ayla.internal.converters.FanSpeedConverter
import org.alberto97.aircontroller.provider.ayla.internal.converters.ModeConverter
import org.alberto97.aircontroller.provider.ayla.internal.converters.SleepModeConverter
import org.alberto97.aircontroller.provider.ayla.internal.converters.TempUnitConverter
import org.alberto97.aircontroller.provider.ayla.internal.repositories.IDeviceCacheRepository
import org.alberto97.aircontroller.provider.ayla.internal.repositories.IDevicePropertyRepository
import org.alberto97.aircontroller.provider.ayla.mock.PropertyData
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

class DeviceControlRepositoryTest {

    private lateinit var repository: DeviceControlRepository

    @Before
    fun setUp() {
        val propertyRepo: IDevicePropertyRepository = mock {
            onBlocking { getProperties(any()) }.thenReturn(PropertyData.properties)
        }
        val prefs: IDeviceCacheRepository = mock()
        repository = DeviceControlRepository(
            propertyRepo,
            prefs,
            FanSpeedConverter(),
            ModeConverter(),
            TempUnitConverter(),
            SleepModeConverter()
        )
    }

    @Test
    fun getDeviceState() = runBlocking {
        val state = repository.getDeviceState("")
        assert(state is ResultWrapper.Success)
        assertEquals(state.data?.productName, "Bedroom")
        assertEquals(state.data?.backlight, true)
        //assertEquals(state.data?.supportedModes, )
        assertEquals(state.data?.workMode, WorkMode.Dry)
        assertEquals(state.data?.horizontalAirFlow, true)
        assertEquals(state.data?.verticalAirFlow, true)
        assertEquals(state.data?.quiet, true)
        assertEquals(state.data?.eco, true)
        assertEquals(state.data?.boost, true)
        assertEquals(state.data?.sleepMode, SleepMode.MODE2)
        //assertEquals(state.data?.supportedSleepModes, )
        //assertEquals(state.data?.supportedSpeeds, )
        assertEquals(state.data?.fanSpeed, FanSpeed.Low)
        assertEquals(state.data?.temp, 68)
        assertEquals(state.data?.roomTemp, 79)
        assertEquals(state.data?.tempUnit, TempType.Fahrenheit)
        assertEquals(state.data?.on, true)
        //assertEquals(state.data?.maxTemp, )
        //assertEquals(state.data?.minTemp, )
    }
}