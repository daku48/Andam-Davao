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
import com.example.andamdavao.data.model.SampleData
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
fun MapScreen() {
    val context = LocalContext.current

    val davaoCityCenter = remember { LatLng(7.0722, 125.6131) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(davaoCityCenter, 12f)
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCenter by remember { mutableStateOf<EvacuationCenter?>(SampleData.sampleCenters.first()) }
    var isLegendExpanded by remember { mutableStateOf(true) }
    var showBarangaySelector by remember { mutableStateOf(false) }
    var selectedBarangay by remember { mutableStateOf("Davao City") }

    var showFloodOverlay by remember { mutableStateOf(true) }
    var showLandslideOverlay by remember { mutableStateOf(false) }
    var showEmergencyFacilities by remember { mutableStateOf(true) }

    var hasLocationPermission by remember { mutableStateOf(LocationUtils.hasLocationPermission(context)) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
                (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
    }

    val emergencyFacilities = remember {
        listOf(
            EmergencyFacility("SPMC Main Hospital", "Hospital", 7.0988, 125.6267),
            EmergencyFacility("BFP Central Fire Station", "Fire Station", 7.0682, 125.6095),
            EmergencyFacility("PNP Davao City Police Office", "Police Station", 7.0631, 125.6028)
        )
    }

    val davaoFilteredCenters = SampleData.sampleCenters.filter { center ->
        searchQuery.isEmpty() ||
                center.centerName.contains(searchQuery, ignoreCase = true) ||
                center.barangay.contains(searchQuery, ignoreCase = true) ||
                center.district.contains(searchQuery, ignoreCase = true)
    }

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
                Marker(
                    state = rememberMarkerState(position = LatLng(center.latitude, center.longitude)),
                    title = center.centerName,
                    snippet = "${center.barangay} • ${center.status}",
                    onClick = {
                        selectedCenter = center
                        true
                    }
                )
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

            if (showFloodOverlay) {
                Polygon(
                    points = listOf(
                        LatLng(7.0500, 125.5900),
                        LatLng(7.0800, 125.6100),
                        LatLng(7.0700, 125.6300),
                        LatLng(7.0400, 125.6100)
                    ),
                    fillColor = HazardFloodRed.copy(alpha = 0.25f),
                    strokeColor = HazardFloodRed,
                    strokeWidth = 2f
                )
            }

            if (showLandslideOverlay) {
                Polygon(
                    points = listOf(
                        LatLng(7.1800, 125.4500),
                        LatLng(7.2200, 125.4800),
                        LatLng(7.2000, 125.5200),
                        LatLng(7.1500, 125.4900)
                    ),
                    fillColor = HazardLandslideOrange.copy(alpha = 0.30f),
                    strokeColor = HazardLandslideOrange,
                    strokeWidth = 2f
                )
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
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            LocationUtils.davaoBarangays.take(4).forEach { bgy ->
                                FilterChip(
                                    selected = selectedBarangay == bgy,
                                    onClick = {
                                        selectedBarangay = bgy
                                        searchQuery = bgy
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
                    MapLegendItem(color = Color(0xFF757575), label = "Status Unknown")

                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = DividerGray)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { showFloodOverlay = !showFloodOverlay }
                    ) {
                        Checkbox(
                            checked = showFloodOverlay,
                            onCheckedChange = { showFloodOverlay = it },
                            colors = CheckboxDefaults.colors(checkedColor = CrimsonRed)
                        )
                        Text("Flood Hazard (MGB Layer)", fontSize = 11.sp, color = TextPrimaryNavy)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { showLandslideOverlay = !showLandslideOverlay }
                    ) {
                        Checkbox(
                            checked = showLandslideOverlay,
                            onCheckedChange = { showLandslideOverlay = it },
                            colors = CheckboxDefaults.colors(checkedColor = HazardLandslideOrange)
                        )
                        Text("Landslide (MGB XI)", fontSize = 11.sp, color = TextPrimaryNavy)
                    }
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
                                text = "${center.barangay}, Davao City • ${center.distanceKm} km away",
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
