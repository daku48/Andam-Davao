package com.example.andamdavao.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.andamdavao.BuildConfig
import com.example.andamdavao.data.local.LocalCache
import com.example.andamdavao.data.model.DataState
import com.example.andamdavao.data.model.EvacuationCenter
import com.example.andamdavao.data.model.PublicAnnouncement
import com.example.andamdavao.data.model.SampleData
import com.example.andamdavao.data.model.WeatherState
import com.example.andamdavao.data.repository.AnnouncementRepository
import com.example.andamdavao.data.repository.EvacuationCenterRepository
import com.example.andamdavao.data.repository.WeatherRepository
import com.example.andamdavao.utils.CenterLogic
import com.example.andamdavao.utils.LocationUtils
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * One ViewModel shared by all screens (obtained with viewModel() from the Activity).
 * Flow: show saved data instantly, refresh from the network, keep saved data if refresh fails.
 */
class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val cache = LocalCache(application)
    private val centerRepo = EvacuationCenterRepository()
    private val announcementRepo = AnnouncementRepository()
    private val weatherRepo = WeatherRepository()

    private val rawCenters = MutableStateFlow(DataState<EvacuationCenter>())
    private val _announcements = MutableStateFlow(DataState<PublicAnnouncement>())
    private val _weather = MutableStateFlow<WeatherState>(WeatherState.Loading)
    private val _userLocation = MutableStateFlow<Pair<Double, Double>?>(null)

    /** Centers with distance filled in and sorted nearest-first once location is known. */
    val centers: StateFlow<DataState<EvacuationCenter>> =
        combine(rawCenters, _userLocation) { state, loc ->
            if (loc == null) state
            else state.copy(items = CenterLogic.withDistances(state.items, loc.first, loc.second))
        }.stateIn(viewModelScope, SharingStarted.Eagerly, DataState())

    val announcements: StateFlow<DataState<PublicAnnouncement>> = _announcements.asStateFlow()
    val weather: StateFlow<WeatherState> = _weather.asStateFlow()

    init {
        refresh()
        startWeatherLoop()
    }

    fun refresh() {
        viewModelScope.launch {
            refreshInto(
                flow = rawCenters,
                loadCache = { cache.loadCenters() },
                saveCache = { cache.saveCenters(it) },
                fetch = { centerRepo.fetchPublicEvacuationCenters() },
                sample = SampleData.sampleCenters
            )
        }
        viewModelScope.launch {
            refreshInto(
                flow = _announcements,
                loadCache = { cache.loadAnnouncements() },
                saveCache = { cache.saveAnnouncements(it) },
                fetch = { announcementRepo.fetchPublicAnnouncements() },
                sample = SampleData.sampleAnnouncements
            )
        }
    }

    private suspend fun <T> refreshInto(
        flow: MutableStateFlow<DataState<T>>,
        loadCache: () -> LocalCache.Cached<List<T>>?,
        saveCache: (List<T>) -> Unit,
        fetch: suspend () -> Result<List<T>>,
        sample: List<T>
    ) {
        // 1. Show saved data immediately, if we have any and nothing is on screen yet.
        if (flow.value.items.isEmpty()) {
            val cached = withContext(Dispatchers.IO) { loadCache() }
            if (cached != null) {
                flow.value = DataState(
                    items = cached.value,
                    isLoading = true,
                    isFromCache = true,
                    lastUpdatedMillis = cached.savedAtMillis
                )
            }
        }
        flow.update { it.copy(isLoading = true) }

        // 2. Ask the server.
        val result = fetch()
        val fresh = result.getOrNull()
        if (fresh != null) {
            withContext(Dispatchers.IO) { saveCache(fresh) }
            flow.value = DataState(
                items = fresh,
                isLoading = false,
                lastUpdatedMillis = System.currentTimeMillis()
            )
            return
        }

        // 3. Failed: keep whatever real data we already have.
        val current = flow.value
        flow.value = when {
            current.items.isNotEmpty() && !current.isSample ->
                current.copy(isLoading = false, isFromCache = true, hasError = true)
            BuildConfig.DEBUG ->
                DataState(items = sample, isLoading = false, isSample = true, hasError = true)
            else ->
                DataState(isLoading = false, hasError = true)
        }
    }

    private fun startWeatherLoop() {
        viewModelScope.launch {
            val saved = withContext(Dispatchers.IO) { cache.loadWeather() }
            if (saved != null && !isTooOld(saved.value.fetchedAtMillis)) {
                _weather.value = WeatherState.Available(saved.value, isStale = true)
            }

            while (isActive) {
                val fresh = weatherRepo.fetchDavaoLiveWeather()
                if (fresh != null) {
                    withContext(Dispatchers.IO) { cache.saveWeather(fresh) }
                    _weather.value = WeatherState.Available(fresh, isStale = false)
                    delay(WEATHER_REFRESH_MS)
                } else {
                    val current = _weather.value
                    _weather.value =
                        if (current is WeatherState.Available && !isTooOld(current.data.fetchedAtMillis)) {
                            current.copy(isStale = true)
                        } else {
                            WeatherState.Unavailable
                        }
                    delay(WEATHER_RETRY_MS)
                }
            }
        }
    }

    private fun isTooOld(fetchedAtMillis: Long) =
        System.currentTimeMillis() - fetchedAtMillis > WEATHER_MAX_STALE_MS

    /** Safe to call any time; does nothing unless location permission is granted. */
    @SuppressLint("MissingPermission")
    fun refreshLocation() {
        val app = getApplication<Application>()
        if (!LocationUtils.hasLocationPermission(app)) return
        try {
            LocationServices.getFusedLocationProviderClient(app)
                .getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    CancellationTokenSource().token
                )
                .addOnSuccessListener { location ->
                    if (location != null) {
                        _userLocation.value = location.latitude to location.longitude
                    }
                }
        } catch (e: SecurityException) {
            // Permission revoked between the check and the call. Ignore.
        }
    }

    private companion object {
        const val WEATHER_REFRESH_MS = 15 * 60_000L
        const val WEATHER_RETRY_MS = 60_000L
        const val WEATHER_MAX_STALE_MS = 3 * 60 * 60_000L
    }
}
