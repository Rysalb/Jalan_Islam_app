package com.jalanislam.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalanislam.app.data.model.PrayerName
import com.jalanislam.app.data.model.PrayerTimeItem
import com.jalanislam.app.data.preferences.NotificationMode
import com.jalanislam.app.ui.theme.IslamicGold
import com.jalanislam.app.ui.theme.TextSecondaryLight

@Composable
fun PrayerScheduleCard(
    item: PrayerTimeItem,
    notificationMode: NotificationMode,
    onSoundToggle: () -> Unit,
    onAdjustClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isNext = item.isNext
    val isImsak = item.name == PrayerName.IMSAK

    val cardBg = if (isNext) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    val borderColor = if (isNext) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onAdjustClick() }
            .border(if (isNext) 1.5.dp else 0.dp, borderColor, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isNext) 3.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Prayer Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getPrayerIcon(item.name),
                    contentDescription = null,
                    tint = if (isNext) Color.White else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Prayer Name & Offset info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = item.name.titleRes),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isNext) FontWeight.Bold else FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (item.offsetMinutes != 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        val offsetText = if (item.offsetMinutes > 0) "+${item.offsetMinutes}m" else "${item.offsetMinutes}m"
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(IslamicGold.copy(alpha = 0.2f))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = offsetText,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                if (isImsak) {
                    Text(
                        text = "Batas Sahur (Ihtiyath)",
                        style = MaterialTheme.typography.labelSmall,
                        color = IslamicGold,
                        fontWeight = FontWeight.Medium
                    )
                } else if (isNext) {
                    Text(
                        text = "Waktu Berikutnya",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Time Display
            Text(
                text = item.formattedTime,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Audio Mode Toggle
            IconButton(
                onClick = onSoundToggle,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = when (notificationMode) {
                        NotificationMode.FULL_ADZAN -> Icons.AutoMirrored.Filled.VolumeUp
                        NotificationMode.BEEP -> Icons.Filled.Notifications
                        NotificationMode.MUTE -> Icons.AutoMirrored.Filled.VolumeOff
                    },
                    contentDescription = "Mode Suara",
                    tint = if (notificationMode == NotificationMode.MUTE) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun getPrayerIcon(name: PrayerName): ImageVector = when (name) {
    PrayerName.IMSAK -> Icons.Filled.Nightlight
    PrayerName.FAJR -> Icons.Filled.WbTwilight
    PrayerName.SUNRISE -> Icons.Filled.WbSunny
    PrayerName.DHUHR -> Icons.Filled.LightMode
    PrayerName.ASR -> Icons.Filled.WbCloudy
    PrayerName.MAGHRIB -> Icons.Filled.NightsStay
    PrayerName.ISHA -> Icons.Filled.Bedtime
}
