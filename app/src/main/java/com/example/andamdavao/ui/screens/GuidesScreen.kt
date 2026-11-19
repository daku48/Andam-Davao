package com.example.andamdavao.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.andamdavao.ui.theme.*

data class DetailedGuide(
    val title: String,
    val icon: ImageVector,
    val summary: String,
    val bulletPoints: List<String>
)

/**
 * Preparedness Guide screen with expandable detailed instructions.
 */
@Composable
fun GuidesScreen() {
    var expandedTitle by remember { mutableStateOf<String?>(null) }

    val guides = listOf(
        DetailedGuide(
            title = "Emergency Go-Bag Checklist",
            icon = Icons.Default.MedicalServices,
            summary = "Pack essential items for 72 hours of self-sufficiency.",
            bulletPoints = listOf(
                "Drinking water (1 gallon per person per day)",
                "Non-perishable food supply (canned goods, energy bars)",
                "First aid kit, prescription medicines, face masks",
                "Flashlight, extra batteries, power bank",
                "Whistle, multi-tool knife, emergency blanket",
                "Important documents (IDs, land titles, insurance) in waterproof bag"
            )
        ),
        DetailedGuide(
            title = "What to Do During Floods",
            icon = Icons.Default.Water,
            summary = "Key safety steps when floodwaters begin to rise.",
            bulletPoints = listOf(
                "Move to higher ground or second floor immediately if trapped",
                "Turn off main electricity switch and LPG gas valves",
                "Do not walk or drive through moving floodwaters",
                "Listen to official DRRMO radio broadcasts for evacuation calls",
                "Avoid contact with electrical wires or submerged poles"
            )
        ),
        DetailedGuide(
            title = "Earthquake Safety (Duck, Cover & Hold)",
            icon = Icons.Default.Home,
            summary = "Immediate actions during strong ground shaking.",
            bulletPoints = listOf(
                "DUCK down onto your hands and knees",
                "COVER your head and neck under a sturdy table or desk",
                "HOLD ON until the shaking completely stops",
                "If outdoors, move away from buildings, streetlights, and utility wires",
                "Check for gas leaks and structural damage before re-entering"
            )
        ),
        DetailedGuide(
            title = "Typhoon Preparedness",
            icon = Icons.Default.Air,
            summary = "Steps to secure your home before typhoon landfall.",
            bulletPoints = listOf(
                "Secure roof sheets, windows, and outdoor furniture",
                "Clean gutters and drainage pathways around your house",
                "Charge phones, power banks, and emergency lights",
                "Identify your nearest verified evacuation center in Andam Davao"
            )
        ),
        DetailedGuide(
            title = "After a Disaster",
            icon = Icons.Default.CheckCircle,
            summary = "Post-disaster safety checks and recovery guidance.",
            bulletPoints = listOf(
                "Return home only when local authorities declare it safe",
                "Inspect home foundation and electrical wiring for damage",
                "Boil drinking water until official water safety advisories are cleared",
                "Report missing persons or damaged public facilities to DRRMO"
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrayBackground)
    ) {
        // Top Header
        Surface(
            color = SurfaceWhite,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = "Guides",
                    tint = PrimaryNavy,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Preparedness Guide",
                    color = PrimaryNavy,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Hero Banner
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Be Prepared for Any Disaster",
                                color = SurfaceWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Know what to do, stay informed, and keep your family safe.",
                                color = IceBlue,
                                fontSize = 13.sp
                            )
                        }

                        Surface(
                            color = CrimsonRed,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(50.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.LocalHospital,
                                    contentDescription = null,
                                    tint = SurfaceWhite,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Guide Cards List
            items(guides) { guide ->
                val isExpanded = expandedTitle == guide.title

                Card(
                    onClick = {
                        expandedTitle = if (isExpanded) null else guide.title
                    },
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    color = IceBlue,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = guide.icon,
                                            contentDescription = null,
                                            tint = PrimaryNavy,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = guide.title,
                                        color = PrimaryNavy,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = guide.summary,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Expand",
                                tint = TextSecondary
                            )
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = DividerGray)
                            Spacer(modifier = Modifier.height(12.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                guide.bulletPoints.forEach { point ->
                                    Row(verticalAlignment = Alignment.Top) {
                                        Text(
                                            text = "• ",
                                            color = CrimsonRed,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = point,
                                            color = TextPrimaryNavy,
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
