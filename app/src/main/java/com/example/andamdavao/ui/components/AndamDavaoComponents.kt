package com.example.andamdavao.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.andamdavao.ui.theme.*

@Composable
fun AndamShieldLogo(
    modifier: Modifier = Modifier,
    showTagline: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PrimaryNavy, RoyalBlue)
                    ),
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp, bottomStart = 55.dp, bottomEnd = 55.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Andam Davao Shield",
                tint = SurfaceWhite,
                modifier = Modifier.size(64.dp)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.7f)
                    .height(6.dp)
                    .background(CrimsonRed, shape = RoundedCornerShape(3.dp))
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "ANDAM",
            color = PrimaryNavy,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )
        Text(
            text = "DAVAO",
            color = CrimsonRed,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )

        if (showTagline) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Be Ready. Be Safe. Be Andam.",
                color = PrimaryNavy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TopWaveAccent(
    modifier: Modifier = Modifier,
    height: Float = 140f
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
    ) {
        val width = size.width
        val h = size.height

        val redPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(width, 0f)
            lineTo(width, h * 0.5f)
            cubicTo(width * 0.7f, h * 0.8f, width * 0.3f, h * 0.2f, 0f, h * 0.6f)
            close()
        }
        drawPath(redPath, color = CrimsonRed)

        val navyPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(width, 0f)
            lineTo(width, h * 0.35f)
            cubicTo(width * 0.6f, h * 0.7f, width * 0.4f, h * 0.15f, 0f, h * 0.45f)
            close()
        }
        drawPath(navyPath, color = PrimaryNavy)
    }
}

@Composable
fun EvacuationStatusBadge(
    statusText: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (statusText.lowercase()) {
        "open" -> StatusVerifiedGreen to SurfaceWhite
        "full" -> StatusCautionAmber to SurfaceWhite
        "closed" -> CrimsonRed to SurfaceWhite
        else -> LightGrayBackground to TextSecondary
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Text(
            text = statusText,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

sealed class NavTab(val route: String, val title: String, val icon: ImageVector, val badgeCount: Int = 0) {
    object Home : NavTab("home", "Home", Icons.Default.Home)
    object Map : NavTab("map", "Map", Icons.Default.LocationOn)
    object Alerts : NavTab("alerts", "Alerts", Icons.Default.Notifications)
    object More : NavTab("more", "More", Icons.Default.Menu)

    companion object {
        fun fromRoute(route: String): NavTab = when (route) {
            "map" -> Map
            "alerts" -> Alerts
            "more" -> More
            else -> Home
        }
    }
}

@Composable
fun AndamBottomNavigation(
    currentRoute: String,
    onTabSelected: (NavTab) -> Unit,
    alertBadgeCount: Int = 0
) {
    val tabs = listOf(NavTab.Home, NavTab.Map, NavTab.Alerts, NavTab.More)

    Surface(
        color = SurfaceWhite,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = currentRoute == tab.route
                val badge = if (tab.route == NavTab.Alerts.route) alertBadgeCount else tab.badgeCount

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = if (isSelected) CrimsonRed else PrimaryNavy,
                            modifier = Modifier.size(24.dp)
                        )

                        if (badge > 0) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (badge > 9) "9+" else badge.toString(),
                                    color = SurfaceWhite,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = tab.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) CrimsonRed else PrimaryNavy
                    )

                    if (isSelected) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(CrimsonRed)
                        )
                    }
                }
            }
        }
    }
}
