package com.example.andamdavao

import com.example.andamdavao.utils.LocationUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationUtilsTest {

    @Test
    fun sameVenue_isZeroKm() {
        val d = LocationUtils.calculateDistanceKm(7.0722, 125.6131, 7.0722, 125.6131)
        assertEquals(0.0, d, 0.0)
    }

    @Test
    fun davaoCenterToCalinan_isAboutTwentyOneKm() {
        val d = LocationUtils.calculateDistanceKm(7.0722, 125.6131, 7.1852, 125.4598)
        assertEquals(21.1, d, 0.2)
    }

    @Test
    fun distanceIsSymmetric() {
        val ab = LocationUtils.calculateDistanceKm(7.0655, 125.6089, 7.0782, 125.6120)
        val ba = LocationUtils.calculateDistanceKm(7.0782, 125.6120, 7.0655, 125.6089)
        assertEquals(ab, ba, 0.0)
    }
}
