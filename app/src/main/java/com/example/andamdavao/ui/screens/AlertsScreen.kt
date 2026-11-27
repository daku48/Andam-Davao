package com.example.andamdavao.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.andamdavao.data.model.PublicAnnouncement
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.andamdavao.ui.AppViewModel
import com.example.andamdavao.ui.components.DataStatusBanner
import com.example.andamdavao.ui.theme.*

@Composable
fun AlertsScreen(
    onAnnouncementSelected: (PublicAnnouncement) -> Unit,
    viewModel: AppViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Latest", "History")

    val state by viewModel.announcements.collectAsState()
    val latestAlerts = state.items.filter { !it.isHistory }
    val historyAlerts = state.items.filter { it.isHistory }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrayBackground)
    ) {
        Surface(
            color = SurfaceWhite,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Alerts",
                        tint = PrimaryNavy,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Emergency Alerts",
                        color = PrimaryNavy,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SurfaceWhite,
                    contentColor = CrimsonRed,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = CrimsonRed
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 14.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == index) CrimsonRed else TextSecondary
                                )
                            }
                        )
                    }
                }
            }
        }

        val currentList = if (selectedTab == 0) latestAlerts else historyAlerts

        DataStatusBanner(
            state = state,
            onRetry = { viewModel.refresh() },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (currentList.isEmpty() && !state.isLoading) {
                item {
                    Text(
                        text = if (selectedTab == 0) "No active announcements right now." else "No past announcements.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            items(currentList, key = { it.id }) { alert ->
                val severityColor = when (alert.severity.lowercase()) {
                    "urgent" -> CrimsonRed
                    "advisory" -> RoyalBlue
                    else -> StatusCautionAmber
                }

                Card(
                    onClick = { onAnnouncementSelected(alert) },
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            color = severityColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Alert",
                                    tint = severityColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = alert.title,
                                color = PrimaryNavy,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = alert.body,
                                color = TextPrimaryNavy,
                                fontSize = 13.sp,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${alert.issuingAgency} • ${alert.publishedAt}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
