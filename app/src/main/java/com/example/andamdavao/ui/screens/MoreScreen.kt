package com.example.andamdavao.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.andamdavao.ui.components.TopWaveAccent
import com.example.andamdavao.ui.theme.*

data class MoreOption(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

/**
 * More / Profile / Guest Settings screen matching Andam Davao visual guidelines.
 */
@Composable
fun MoreScreen(
    onSignInClicked: () -> Unit
) {
    val options = listOf(
        MoreOption("Favorites", Icons.Default.Favorite) { },
        MoreOption("Notification Settings", Icons.Default.Notifications) { },
        MoreOption("About Andam Davao", Icons.Default.Info) { },
        MoreOption("Settings", Icons.Default.Settings) { },
        MoreOption("Help & Support", Icons.Default.Help) { }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrayBackground)
    ) {
        TopWaveAccent(height = 160f)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Guest User Card Banner
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = SurfaceWhite,
                        shape = CircleShape,
                        shadowElevation = 4.dp,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Guest",
                                tint = PrimaryNavy,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Guest User",
                        color = PrimaryNavy,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Continue as Guest",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Options List
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        options.forEachIndexed { index, option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { option.onClick() }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = option.icon,
                                        contentDescription = option.title,
                                        tint = PrimaryNavy,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Text(
                                        text = option.title,
                                        color = TextPrimaryNavy,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Open",
                                    tint = TextSecondary
                                )
                            }

                            if (index < options.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = DividerGray
                                )
                            }
                        }
                    }
                }
            }

            // Sign In Action Button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onSignInClicked,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Sign In",
                        color = SurfaceWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // No Account Required Note
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = IceBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = RoyalBlue,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "No account required to access essential emergency information.",
                            color = PrimaryNavy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
