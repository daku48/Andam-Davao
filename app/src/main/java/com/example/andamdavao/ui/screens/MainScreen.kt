package com.example.andamdavao.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.andamdavao.data.model.EvacuationCenter
import com.example.andamdavao.data.model.PublicAnnouncement
import com.example.andamdavao.ui.components.AndamBottomNavigation
import com.example.andamdavao.ui.components.NavTab

/**
 * MainScreen housing the bottom navigation bar and managing screen stacks.
 */
@Composable
fun MainScreen(
    onSignInClicked: () -> Unit
) {
    var currentTab by remember { mutableStateOf<NavTab>(NavTab.Home) }

    // Sub-screen navigation states
    var selectedCenter by remember { mutableStateOf<EvacuationCenter?>(null) }
    var selectedAnnouncement by remember { mutableStateOf<PublicAnnouncement?>(null) }
    var isDirectoryActive by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            AndamBottomNavigation(
                currentRoute = currentTab.route,
                onTabSelected = { tab ->
                    currentTab = tab
                    // Reset sub-screen navigation when clicking bottom tabs
                    selectedCenter = null
                    selectedAnnouncement = null
                    isDirectoryActive = false
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                // Center Detail View Overlay
                selectedCenter != null -> {
                    EvacuationCenterDetailScreen(
                        center = selectedCenter!!,
                        onBackClicked = { selectedCenter = null }
                    )
                }

                // Announcement Detail View Overlay
                selectedAnnouncement != null -> {
                    AlertDetailScreen(
                        announcement = selectedAnnouncement!!,
                        onBackClicked = { selectedAnnouncement = null }
                    )
                }

                // Directory View Overlay
                isDirectoryActive -> {
                    EvacuationDirectoryScreen(
                        onCenterSelected = { center ->
                            selectedCenter = center
                        },
                        onBackClicked = { isDirectoryActive = false }
                    )
                }

                // Main Tab Screens
                else -> {
                    when (currentTab) {
                        NavTab.Home -> HomeScreen(
                            onNavigateToMap = { currentTab = NavTab.Map },
                            onNavigateToCenters = { isDirectoryActive = true },
                            onNavigateToHotlines = { currentTab = NavTab.More },
                            onNavigateToGuides = { currentTab = NavTab.More },
                            onAnnouncementSelected = { announcement ->
                                selectedAnnouncement = announcement
                            }
                        )

                        NavTab.Map -> MapScreen()

                        NavTab.Alerts -> AlertsScreen(
                            onAnnouncementSelected = { announcement ->
                                selectedAnnouncement = announcement
                            }
                        )

                        NavTab.More -> MoreScreen(
                            onSignInClicked = onSignInClicked
                        )
                    }
                }
            }
        }
    }
}
