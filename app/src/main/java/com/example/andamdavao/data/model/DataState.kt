package com.example.andamdavao.data.model

/**
 * Generic holder for a list loaded from the network with an offline cache behind it.
 *
 * isFromCache: the last network refresh failed and we are showing saved data.
 * isSample:    debug builds only, shows built-in sample data (never in release).
 */
data class DataState<T>(
    val items: List<T> = emptyList(),
    val isLoading: Boolean = true,
    val isFromCache: Boolean = false,
    val isSample: Boolean = false,
    val lastUpdatedMillis: Long? = null,
    val hasError: Boolean = false
) {
    val isEmpty: Boolean get() = items.isEmpty()
}

sealed interface WeatherState {
    data object Loading : WeatherState
    data object Unavailable : WeatherState
    data class Available(val data: WeatherData, val isStale: Boolean) : WeatherState
}
