package com.example.andamdavao.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.andamdavao.data.model.EvacuationCenter
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.andamdavao.ui.AppViewModel
import com.example.andamdavao.ui.components.DataStatusBanner
import com.example.andamdavao.utils.CenterLogic
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.example.andamdavao.ui.components.EvacuationStatusBadge
import com.example.andamdavao.ui.theme.*
import com.example.andamdavao.utils.LocationUtils
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

data class EmergencyFacility(
    val name: String,
    val type: String,
    val latitude: Double,
    val longitude: Double
)

@Composable
fun MapScreen(viewModel: AppViewModel = viewModel()) {
    val context = LocalContext.current

    val davaoCityCenter = remember { LatLng(7.0722, 125.6131) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(davaoCityCenter, 12f)
    }

    var searchQuery by remember { mutableStateOf("") }
    val centersState by viewModel.centers.collectAsState()
    var selectedCenterId by rememberSaveable { mutableStateOf<String?>(null) }
    val selectedCenter: EvacuationCenter? =
        selectedCenterId?.let { id -> centersState.items.firstOrNull { it.id == id } }
    var isLegendExpanded by remember { mutableStateOf(true) }
    var showBarangaySelector by remember { mutableStateOf(false) }
    var selectedBarangay by remember { mutableStateOf("All") }

    var showEmergencyFacilities by remember { mutableStateOf(true) }

    var hasLocationPermission by remember { mutableStateOf(LocationUtils.hasLocationPermission(context)) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
                (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) viewModel.refreshLocation()
    }

    val emergencyFacilities = remember {
        listOf(
            EmergencyFacility("SPMC Main Hospital", "Hospital", 7.0988, 125.6267),
            EmergencyFacility("BFP Central Fire Station", "Fire Station", 7.0682, 125.6095),
            EmergencyFacility("PNP Davao City Police Office", "Police Station", 7.0631, 125.6028)
        )
    }

    val davaoFilteredCenters = CenterLogic.filter(centersState.items, searchQuery)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrayBackground)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                compassEnabled = true
            ),
            properties = MapProperties(
                isMyLocationEnabled = hasLocationPermission
            )
        ) {
            davaoFilteredCenters.forEach { center ->
                key(center.id) {
                    Marker(
                        state = rememberMarkerState(position = LatLng(center.latitude, center.longitude)),
                        title = center.centerName,
                        snippet = "${center.barangay} \u00B7 ${center.status}",
                        icon = BitmapDescriptorFactory.defaultMarker(markerHue(center.status)),
                        onClick = {
                            selectedCenterId = center.id
                            true
                        }
                    )
                }
            }

            if (showEmergencyFacilities) {
                emergencyFacilities.forEach { facility ->
                    Marker(
                        state = rememberMarkerState(position = LatLng(facility.latitude, facility.longitude)),
                        title = facility.name,
                        snippet = facility.type
                    )
                }
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search Davao centers or barangay...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextSecondary
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedBorderColor = RoyalBlue,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Surface(
                    color = SurfaceWhite,
                    shape = CircleShape,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(48.dp)
                ) {
                    IconButton(onClick = { showBarangaySelector = !showBarangaySelector }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Barangay Filter",
                            tint = PrimaryNavy
                        )
                    }
                }
            }

            if (showBarangaySelector) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Davao City Barangay Selection",
                            color = PrimaryNavy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(listOf("All") + LocationUtils.davaoBarangays) { bgy ->
                                FilterChip(
                                    selected = selectedBarangay == bgy,
                                    onClick = {
                                        selectedBarangay = bgy
                                        searchQuery = if (bgy == "All") "" else bgy
                                        showBarangaySelector = false
                                    },
                                    label = { Text(bgy, fontSize = 11.sp) },
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        DataStatusBanner(
            state = centersState,
            onRetry = { viewModel.refresh() },
            emptyText = "No verified evacuation centers available yet.",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp, start = 16.dp, end = 16.dp)
        )

        if (isLegendExpanded) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite.copy(alpha = 0.95f)),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp, top = 80.dp)
                    .width(220.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Davao Map Legend",
                            color = PrimaryNavy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Legend",
                            tint = TextSecondary,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { isLegendExpanded = false }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    MapLegendItem(color = StatusVerifiedGreen, label = "Open Center")
                    MapLegendItem(color = StatusCautionAmber, label = "Full Center")
                    MapLegendItem(color = CrimsonRed, label = "Closed Center")
                    MapLegendItem(color = Color(0xFF8E24AA), label = "Status Unknown")
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isLegendExpanded) {
                Surface(
                    color = SurfaceWhite,
                    shape = CircleShape,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(42.dp)
                ) {
                    IconButton(onClick = { isLegendExpanded = true }) {
                        Icon(imageVector = Icons.Default.LegendToggle, contentDescription = "Legend", tint = PrimaryNavy)
                    }
                }
            }

            Surface(
                color = SurfaceWhite,
                shape = CircleShape,
                shadowElevation = 4.dp,
                modifier = Modifier.size(42.dp)
            ) {
                IconButton(onClick = {
                    if (!hasLocationPermission) {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }) {
                    Icon(
                        imageVector = if (hasLocationPermission) Icons.Default.MyLocation else Icons.Default.LocationOff,
                        contentDescription = "My Location",
                        tint = if (hasLocationPermission) RoyalBlue else TextSecondary
                    )
                }
            }
        }

        selectedCenter?.let { center ->
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = center.centerName,
                                color = PrimaryNavy,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${center.barangay}, Davao City • ${center.distanceKm?.let { "$it km away" } ?: "distance unknown"}",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        EvacuationStatusBadge(statusText = center.status)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Verified ${center.verifiedAt} by ${center.verifiedBy}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            LocationUtils.openExactLocationInMap(
                                context = context,
                                latitude = center.latitude,
                                longitude = center.longitude,
                                label = center.centerName
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Navigation,
                                contentDescription = null,
                                tint = SurfaceWhite,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Get Directions (Exact Davao Pin)",
                                color = SurfaceWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun markerHue(status: String): Float = when (status.lowercase()) {
    "open" -> BitmapDescriptorFactory.HUE_GREEN
    "full" -> BitmapDescriptorFactory.HUE_ORANGE
    "closed" -> BitmapDescriptorFactory.HUE_RED
    else -> BitmapDescriptorFactory.HUE_VIOLET
}

@Composable
fun MapLegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = TextPrimaryNavy,
            fontSize = 11.sp
        )
    }
}
