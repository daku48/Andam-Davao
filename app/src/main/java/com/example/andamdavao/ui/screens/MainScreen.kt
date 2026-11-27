package com.example.andamdavao.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.andamdavao.ui.AppViewModel
import com.example.andamdavao.ui.components.AndamBottomNavigation
import com.example.andamdavao.ui.components.NavTab

private const val SUB_DIRECTORY = "directory"
private const val SUB_HOTLINES = "hotlines"
private const val SUB_GUIDES = "guides"

@Composable
fun MainScreen(
    onSignInClicked: () -> Unit,
    viewModel: AppViewModel = viewModel()
) {
    // Only ids and strings are saved, so state survives rotation and process death.
    var currentTabRoute by rememberSaveable { mutableStateOf(NavTab.Home.route) }
    var selectedCenterId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedAnnouncementId by rememberSaveable { mutableStateOf<String?>(null) }
    var subScreen by rememberSaveable { mutableStateOf<String?>(null) }
    var directoryQuery by rememberSaveable { mutableStateOf("") }

    val centersState by viewModel.centers.collectAsState()
    val announcementsState by viewModel.announcements.collectAsState()

    val currentTab = NavTab.fromRoute(currentTabRoute)
    val selectedCenter = selectedCenterId?.let { id -> centersState.items.firstOrNull { it.id == id } }
    val selectedAnnouncement = selectedAnnouncementId?.let { id ->
        announcementsState.items.firstOrNull { it.id == id }
    }
    val activeAlertCount = announcementsState.items.count { !it.isHistory }

    // System back button: close detail, then sub screen, then go back to Home.
    BackHandler(
        enabled = selectedCenter != null || selectedAnnouncement != null ||
                subScreen != null || currentTab != NavTab.Home
    ) {
        when {
            selectedCenter != null -> selectedCenterId = null
            selectedAnnouncement != null -> selectedAnnouncementId = null
            subScreen != null -> subScreen = null
            else -> currentTabRoute = NavTab.Home.route
        }
    }

    Scaffold(
        bottomBar = {
            AndamBottomNavigation(
                currentRoute = currentTab.route,
                alertBadgeCount = activeAlertCount,
                onTabSelected = { tab ->
                    currentTabRoute = tab.route
                    selectedCenterId = null
                    selectedAnnouncementId = null
                    subScreen = null
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
                selectedCenter != null -> {
                    EvacuationCenterDetailScreen(
                        center = selectedCenter,
                        onBackClicked = { selectedCenterId = null }
                    )
                }

                selectedAnnouncement != null -> {
                    AlertDetailScreen(
                        announcement = selectedAnnouncement,
                        onBackClicked = { selectedAnnouncementId = null }
                    )
                }

                subScreen == SUB_DIRECTORY -> {
                    EvacuationDirectoryScreen(
                        initialQuery = directoryQuery,
                        onCenterSelected = { center -> selectedCenterId = center.id },
                        onBackClicked = { subScreen = null },
                        viewModel = viewModel
                    )
                }

                subScreen == SUB_HOTLINES -> HotlinesScreen()

                subScreen == SUB_GUIDES -> GuidesScreen()

                else -> {
                    when (currentTab) {
                        NavTab.Home -> HomeScreen(
                            onNavigateToMap = { currentTabRoute = NavTab.Map.route },
                            onNavigateToCenters = { query ->
                                directoryQuery = query
                                subScreen = SUB_DIRECTORY
                            },
                            onNavigateToHotlines = { subScreen = SUB_HOTLINES },
                            onNavigateToGuides = { subScreen = SUB_GUIDES },
                            onNavigateToAlerts = { currentTabRoute = NavTab.Alerts.route },
                            onAnnouncementSelected = { selectedAnnouncementId = it.id },
                            viewModel = viewModel
                        )

                        NavTab.Map -> MapScreen(viewModel = viewModel)

                        NavTab.Alerts -> AlertsScreen(
                            onAnnouncementSelected = { selectedAnnouncementId = it.id },
                            viewModel = viewModel
                        )

                        NavTab.More -> MoreScreen(
                            onSignInClicked = onSignInClicked,
                            onHotlinesClicked = { subScreen = SUB_HOTLINES },
                            onGuidesClicked = { subScreen = SUB_GUIDES }
                        )
                    }
                }
            }
        }
    }
}
