package com.example.andamdavao.data.local

import android.content.Context
import com.example.andamdavao.data.model.EvacuationCenter
import com.example.andamdavao.data.model.PublicAnnouncement
import com.example.andamdavao.data.model.WeatherData

/** Last-known-good data so the app still shows something with no network. */
class LocalCache(context: Context) {

    data class Cached<T>(val value: T, val savedAtMillis: Long)

    private val prefs = context.applicationContext
        .getSharedPreferences("andam_cache", Context.MODE_PRIVATE)

    fun saveCenters(items: List<EvacuationCenter>) =
        save(KEY_CENTERS, JsonCodec.centersToJson(items))

    fun loadCenters(): Cached<List<EvacuationCenter>>? =
        load(KEY_CENTERS) { JsonCodec.centersFromJson(it) }

    fun saveAnnouncements(items: List<PublicAnnouncement>) =
        save(KEY_ANNOUNCEMENTS, JsonCodec.announcementsToJson(items))

    fun loadAnnouncements(): Cached<List<PublicAnnouncement>>? =
        load(KEY_ANNOUNCEMENTS) { JsonCodec.announcementsFromJson(it) }

    fun saveWeather(weather: WeatherData) =
        save(KEY_WEATHER, JsonCodec.weatherToJson(weather))

    fun loadWeather(): Cached<WeatherData>? =
        load(KEY_WEATHER) { JsonCodec.weatherFromJson(it) }

    private fun save(key: String, json: String) {
        prefs.edit()
            .putString(key, json)
            .putLong(key + TIME_SUFFIX, System.currentTimeMillis())
            .apply()
    }

    private fun <T> load(key: String, decode: (String) -> T): Cached<T>? {
        val json = prefs.getString(key, null) ?: return null
        return try {
            Cached(decode(json), prefs.getLong(key + TIME_SUFFIX, 0L))
        } catch (e: Exception) {
            null
        }
    }

    private companion object {
        const val KEY_CENTERS = "centers"
        const val KEY_ANNOUNCEMENTS = "announcements"
        const val KEY_WEATHER = "weather"
        const val TIME_SUFFIX = "_saved_at"
    }
}
