package com.example.andamdavao

import com.example.andamdavao.data.local.JsonCodec
import com.example.andamdavao.data.model.PublicAnnouncement
import com.example.andamdavao.data.model.WeatherData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class JsonCodecTest {

    @Test
    fun centers_roundTrip() {
        val original = listOf(
            testCenter("a", name = "Gym, \"quoted\" name", status = "Full"),
            testCenter("b", lat = 7.2, lon = 125.5)
        )
        val back = JsonCodec.centersFromJson(JsonCodec.centersToJson(original))
        assertEquals(original, back)
    }

    @Test
    fun announcements_roundTrip() {
        val original = listOf(
            PublicAnnouncement(
                id = "1", title = "Flood Warning", body = "Stay alert", severity = "Urgent",
                affectedAreas = "Matina", issuingAgency = "DRRMO", sourceUrl = "https://example.org",
                publishedAt = "2026-11-19", expiresAt = "2026-11-20", isHistory = false
            )
        )
        val back = JsonCodec.announcementsFromJson(JsonCodec.announcementsToJson(original))
        assertEquals(original, back)
    }

    @Test
    fun weather_roundTrip_keepsNulls() {
        val w = WeatherData(29.0, "Clear Skies", null, 10.0, null, 123L, false)
        val back = JsonCodec.weatherFromJson(JsonCodec.weatherToJson(w))
        assertEquals(w, back)
        assertNull(back.humidity)
    }
}
