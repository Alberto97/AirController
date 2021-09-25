package org.alberto97.aircontroller

import org.alberto97.aircontroller.common.features.TemperatureExtensions.toC
import org.alberto97.aircontroller.common.features.TemperatureExtensions.toF

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TemperatureUnitTest {
    @Test
    fun celsius_to_fahrenheit() {
        assertEquals(16.toF(), 61)
        assertEquals(17.toF(), 63)
        assertEquals(18.toF(), 64)
        assertEquals(19.toF(), 66)
        assertEquals(20.toF(), 68)
        assertEquals(21.toF(), 70)
        assertEquals(22.toF(), 72)
        assertEquals(23.toF(), 73)
        assertEquals(24.toF(), 75)
        assertEquals(25.toF(), 77)
        assertEquals(26.toF(), 79)
        assertEquals(27.toF(), 81)
    }

    @Test
    fun fahrenheit_to_celsius() {
        assertEquals(61.toC(), 16)
        assertEquals(63.toC(), 17)
        assertEquals(64.toC(), 18)
        assertEquals(66.toC(), 19)
        assertEquals(68.toC(), 20)
        assertEquals(70.toC(), 21)
        assertEquals(72.toC(), 22)
        assertEquals(73.toC(), 23)
        assertEquals(75.toC(), 24)
        assertEquals(77.toC(), 25)
        assertEquals(79.toC(), 26)
        assertEquals(81.toC(), 27)
    }
}
