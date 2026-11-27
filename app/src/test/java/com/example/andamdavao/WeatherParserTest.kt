package com.example.andamdavao

import com.example.andamdavao.utils.WeatherParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherParserTest {

    private val goodJson = """
        {"current":{"temperature_2m":29.4,"relative_humidity_2m":78,"weather_code":63,"wind_speed_10m":12.6},
         "hourly":{"precipitation_probability":[65]}}
    """.trimIndent()

    @Test
    fun parsesRealValues() {
        val w = WeatherParser.parse(goodJson, nowMillis = 1000L)!!
        assertEquals(29.4, w.temperature, 0.0)
        assertEquals(78, w.humidity)
        assertEquals(65, w.rainProbability)
        assertEquals(12.6, w.windSpeed, 0.0)
        assertEquals(1000L, w.fetchedAtMillis)
        assertTrue(w.isAlertLevel)
    }

    @Test
    fun missingOptionalFields_becomeNullNotFakeNumbers() {
        val json = """{"current":{"temperature_2m":30.0,"weather_code":0,"wind_speed_10m":5.0}}"""
        val w = WeatherParser.parse(json, 0L)!!
        assertNull(w.humidity)
        assertNull(w.rainProbability)
        assertFalse(w.isAlertLevel)
    }

    @Test
    fun garbageReturnsNull() {
        assertNull(WeatherParser.parse("not json", 0L))
        assertNull(WeatherParser.parse("{}", 0L))
    }

    @Test
    fun weatherCodes() {
        assertEquals("Clear Skies", WeatherParser.decodeWeatherCode(0).first)
        assertTrue(WeatherParser.decodeWeatherCode(95).second)
        assertFalse(WeatherParser.decodeWeatherCode(2).second)
        assertEquals("Unsettled Weather", WeatherParser.decodeWeatherCode(1234).first)
    }
}
