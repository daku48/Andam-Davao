package com.example.andamdavao.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.andamdavao.data.model.DataState
import com.example.andamdavao.data.model.WeatherState
import com.example.andamdavao.ui.theme.*
import com.example.andamdavao.utils.TimeUtils

/** Tells the user when they are looking at saved, sample or missing data. Hidden when all is well. */
@Composable
fun DataStatusBanner(
    state: DataState<*>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    emptyText: String? = null
) {
    val message: String? = when {
        state.isSample ->
            "Sample data (debug build only). This is not real information."
        state.isFromCache && !state.isLoading -> {
            val ago = state.lastUpdatedMillis?.takeIf { it > 0L }
                ?.let { " from ${TimeUtils.formatAgo(it)}" } ?: ""
            "Can't reach the server. Showing saved data$ago."
        }
        state.hasError && state.isEmpty && !state.isLoading ->
            "Couldn't load data. Check your connection and try again."
        state.isEmpty && !state.isLoading && emptyText != null -> emptyText
        else -> null
    }
    if (message == null) return

    val showRetry = state.hasError && !state.isLoading

    Surface(
        color = Color(0xFFFFF4E5),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = null,
                tint = StatusCautionAmber,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = TextPrimaryNavy,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
            if (showRetry) {
                TextButton(onClick = onRetry) {
                    Text("Retry", color = CrimsonRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun WeatherCard(
    state: WeatherState,
    modifier: Modifier = Modifier
) {
    val alert = state is WeatherState.Available && state.data.isAlertLevel
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (alert) Color(0xFFFDE8E8) else SurfaceWhite
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                tint = if (alert) CrimsonRed else RoyalBlue,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))

            when (state) {
                WeatherState.Loading -> Text(
                    text = "Loading weather...",
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                WeatherState.Unavailable -> Text(
                    text = "Weather unavailable right now.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                is WeatherState.Available -> {
                    val w = state.data
                    Column {
                        Text(
                            text = "Davao City  ${w.temperature.toInt()}\u00B0C  ${w.condition}",
                            color = if (alert) CrimsonRed else PrimaryNavy,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        val details = buildList {
                            add("Wind ${w.windSpeed.toInt()} km/h")
                            w.humidity?.let { add("Humidity $it%") }
                            w.rainProbability?.let { add("Rain chance $it%") }
                        }.joinToString("  |  ")
                        Text(text = details, color = TextPrimaryNavy, fontSize = 12.sp)
                        val ago = TimeUtils.formatAgo(w.fetchedAtMillis)
                        Text(
                            text = if (state.isStale) "Saved $ago (can't refresh)" else "Updated $ago",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
