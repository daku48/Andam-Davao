package com.example.andamdavao.data.model

data class WeatherData(
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val rainProbability: Int,
    val updatedAt: String,
    val isAlertLevel: Boolean = false
)
