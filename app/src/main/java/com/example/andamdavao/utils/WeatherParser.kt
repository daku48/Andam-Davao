package com.example.andamdavao.utils

import com.example.andamdavao.data.model.WeatherData
import org.json.JSONObject

object WeatherParser {

    /** Returns a readable condition and whether it deserves attention (rain or storm). */
    fun decodeWeatherCode(code: Int): Pair<String, Boolean> = when (code) {
        0 -> "Clear Skies" to false
        1, 2, 3 -> "Partly Cloudy" to false
        45, 48 -> "Foggy Conditions" to false
        51, 53, 55 -> "Light Rain / Drizzle" to false
        61 -> "Light Rain" to false
        63, 65 -> "Moderate to Heavy Rain" to true
        80, 81, 82 -> "Rain Showers" to true
        95, 96, 99 -> "Thunderstorm" to true
        else -> "Unsettled Weather" to false
    }

    /** Parses an Open-Meteo response. Returns null if the payload is unusable. */
    fun parse(json: String, nowMillis: Long): WeatherData? {
        return try {
            val root = JSONObject(json)
            val current = root.getJSONObject("current")
            val (condition, isAlert) = decodeWeatherCode(current.getInt("weather_code"))

            val humidity = if (current.isNull("relative_humidity_2m")) null
            else current.getInt("relative_humidity_2m")

            val rainArray = root.optJSONObject("hourly")?.optJSONArray("precipitation_probability")
            val rain = if (rainArray != null && rainArray.length() > 0 && !rainArray.isNull(0)) {
                rainArray.getInt(0)
            } else null

            WeatherData(
                temperature = current.getDouble("temperature_2m"),
                condition = condition,
                humidity = humidity,
                windSpeed = current.getDouble("wind_speed_10m"),
                rainProbability = rain,
                fetchedAtMillis = nowMillis,
                isAlertLevel = isAlert
            )
        } catch (e: Exception) {
            null
        }
    }
}
