package com.example.andamdavao.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.andamdavao.ui.components.EvacuationStatusBadge
import com.example.andamdavao.ui.theme.*

/**
 * Evacuation Map Screen UI matching the final visual specification.
 */
@Composable
fun MapScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var isLegendExpanded by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrayBackground)
    ) {
        // Simulated Interactive Map Surface
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE5ECF6))
        ) {
            // Simulated Map Features (Streets, Water, Hazard area indicators)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Davao City Evacuation Map",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Map Provider Integration Ready (Phase 4)",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Top Search Bar & Layer Controls Overlay
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
                    placeholder = { Text("Search evacuation map...", fontSize = 13.sp) },
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

                // Map Layer Toggle Button
                Surface(
                    color = SurfaceWhite,
                    shape = CircleShape,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(48.dp)
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = "Map Layers",
                            tint = PrimaryNavy
                        )
                    }
                }
            }
        }

        // Map Legend Overlay (Bottom Left)
        if (isLegendExpanded) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite.copy(alpha = 0.95f)),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp, top = 80.dp)
                    .width(190.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Map Legend",
                            color = PrimaryNavy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { isLegendExpanded = false }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LegendItem(color = StatusVerifiedGreen, label = "Evacuation Center")
                    LegendItem(color = HazardFloodRed, label = "Flood-Prone Area")
                    LegendItem(color = HazardLandslideOrange, label = "Landslide-Prone Area")
                    LegendItem(color = RoyalBlue, label = "Emergency Facility")
                }
            }
        }

        // Floating Action Controls (Zoom / Current Location)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = SurfaceWhite,
                shape = CircleShape,
                shadowElevation = 4.dp,
                modifier = Modifier.size(42.dp)
            ) {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Zoom In", tint = PrimaryNavy)
                }
            }
            Surface(
                color = SurfaceWhite,
                shape = CircleShape,
                shadowElevation = 4.dp,
                modifier = Modifier.size(42.dp)
            ) {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Zoom Out", tint = PrimaryNavy)
                }
            }
            Surface(
                color = SurfaceWhite,
                shape = CircleShape,
                shadowElevation = 4.dp,
                modifier = Modifier.size(42.dp)
            ) {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.MyLocation, contentDescription = "My Location", tint = RoyalBlue)
                }
            }
        }

        // Bottom Selected Evacuation Center Card Preview
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
                    Column {
                        Text(
                            text = "Rizal Memorial Coliseum",
                            color = PrimaryNavy,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "1.2 km • Davao City",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    EvacuationStatusBadge(statusText = "Open")
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { },
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
                            text = "Get Directions",
                            color = SurfaceWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
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
