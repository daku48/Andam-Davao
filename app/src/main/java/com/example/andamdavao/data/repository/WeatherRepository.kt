package com.example.andamdavao.data.repository

import com.example.andamdavao.data.model.WeatherData
import com.example.andamdavao.utils.WeatherParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class WeatherRepository {

    /** Returns null when weather can't be fetched. The UI shows "unavailable", never fake values. */
    suspend fun fetchDavaoLiveWeather(): WeatherData? = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        try {
            connection = (URL(API_URL).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
            }
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                return@withContext null
            }
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            WeatherParser.parse(body, System.currentTimeMillis())
        } catch (e: Exception) {
            null
        } finally {
            connection?.disconnect()
        }
    }

    private companion object {
        const val API_URL = "https://api.open-meteo.com/v1/forecast" +
                "?latitude=7.0722&longitude=125.6131" +
                "&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m" +
                "&hourly=precipitation_probability&forecast_hours=1" +
                "&timezone=Asia%2FManila"
    }
}
