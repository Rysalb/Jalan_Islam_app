package com.jalanislam.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalanislam.app.R
import com.jalanislam.app.data.model.DailyPrayerSchedule
import com.jalanislam.app.ui.components.EmptyStateView
import com.jalanislam.app.ui.components.ZeroSearchResultsView
import com.jalanislam.app.ui.theme.IslamicGold
import com.jalanislam.app.ui.theme.getThemedTopBarColor

@Composable
fun MonthlyScheduleScreen(
    monthlySchedules: List<DailyPrayerSchedule>,
    locationName: String,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredSchedules = remember(monthlySchedules, searchQuery) {
        if (searchQuery.isBlank()) {
            monthlySchedules
        } else {
            val q = searchQuery.trim().lowercase()
            monthlySchedules.filter {
                it.dateGregorian.lowercase().contains(q) ||
                        it.dateHijri.lowercase().contains(q)
            }
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(getThemedTopBarColor())
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        tint = IslamicGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.nav_monthly),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "30 Hari ke Depan • Wilayah $locationName",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari tanggal atau hari...") },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 6. Zero Search Results State (Agent.MD rule)
            if (filteredSchedules.isEmpty() && searchQuery.isNotEmpty()) {
                item {
                    ZeroSearchResultsView(
                        query = searchQuery,
                        onResetQuery = { searchQuery = "" },
                        onFallbackAction = { searchQuery = "" }
                    )
                }
            }

            // Empty state if list is empty
            if (monthlySchedules.isEmpty()) {
                item {
                    EmptyStateView(
                        title = "Jadwal Belum Tersedia",
                        description = "Silakan tentukan lokasi pada menu Beranda untuk menghitung jadwal 30 hari ke depan.",
                        actionLabel = "Muat Jadwal",
                        onActionClick = {}
                    )
                }
            }

            items(filteredSchedules) { schedule ->
                MonthlyScheduleRowCard(schedule = schedule)
            }
        }
    }
}

@Composable
private fun MonthlyScheduleRowCard(
    schedule: DailyPrayerSchedule
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = schedule.dateGregorian,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = schedule.dateHijri,
                    style = MaterialTheme.typography.labelSmall,
                    color = IslamicGold,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(10.dp))

            // Time grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PrayerMiniColumn("Imsak", schedule.imsak.formattedTime, highlight = true)
                PrayerMiniColumn("Subuh", schedule.fajr.formattedTime)
                PrayerMiniColumn("Dzuhur", schedule.dhuhr.formattedTime)
                PrayerMiniColumn("Ashar", schedule.asr.formattedTime)
                PrayerMiniColumn("Maghrib", schedule.maghrib.formattedTime)
                PrayerMiniColumn("Isya", schedule.isha.formattedTime)
            }
        }
    }
}

@Composable
private fun PrayerMiniColumn(
    label: String,
    time: String,
    highlight: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (highlight) IslamicGold else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = time,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp
        )
    }
}
