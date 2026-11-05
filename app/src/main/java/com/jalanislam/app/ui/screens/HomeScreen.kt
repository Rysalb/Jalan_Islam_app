package com.jalanislam.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jalanislam.app.R
import com.jalanislam.app.data.model.DailyPrayerSchedule
import com.jalanislam.app.data.model.PrayerAdjustment
import com.jalanislam.app.data.model.PrayerName
import com.jalanislam.app.data.preferences.NotificationMode
import com.jalanislam.app.data.quran.QuranReadingProgress
import com.jalanislam.app.ui.components.*
import com.jalanislam.app.ui.theme.IslamicGold
import com.jalanislam.app.ui.theme.getThemedTopBarColor
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    dailySchedule: DailyPrayerSchedule?,
    isLoading: Boolean,
    errorMessage: String?,
    hasLocationPermission: Boolean,
    isOnline: Boolean,
    isGpsActive: Boolean = false,
    isGpsScanning: Boolean = false,
    gpsStatusMessage: String? = null,
    quranProgress: QuranReadingProgress?,
    notificationModes: Map<PrayerName, NotificationMode>,
    onSoundToggle: (PrayerName) -> Unit,
    onBatchNotificationChange: (NotificationMode) -> Unit,
    onTriggerGpsScan: () -> Unit,
    onOpenAdjustDialog: () -> Unit,
    onOpenAdzanPlaylist: (PrayerName) -> Unit,
    onOpenQuranClick: () -> Unit,
    onOpenPrayerGuide: () -> Unit,
    onRequestLocationPermission: () -> Unit,
    onRetry: () -> Unit,
    onSelectCityClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var showExplanationDialog by remember { mutableStateOf(false) }
    var showBatchMenu by remember { mutableStateOf(false) }

    // Ticking timer for live countdown
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentTimeMillis = System.currentTimeMillis()
        }
    }

    if (showExplanationDialog) {
        NotificationModeExplanationDialog(
            onDismiss = { showExplanationDialog = false }
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(getThemedTopBarColor())
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "Logo Jalan Islam",
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f, fill = false)) {
                            Text(
                                text = "Jalan Islam",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { onSelectCityClick() }
                                    .padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = if (isGpsActive) Icons.Filled.MyLocation else Icons.Filled.LocationOn,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = dailySchedule?.locationName ?: if (isGpsActive) "Memindai GPS..." else "Pilih Lokasi",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.92f),
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                                if (isGpsActive) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(IslamicGold.copy(alpha = 0.3f))
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "GPS",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            onClick = { onOpenAdzanPlaylist(PrayerName.FAJR) },
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.18f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.MusicNote,
                                    contentDescription = "Pilihan Suara Adzan",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Surface(
                            onClick = onOpenAdjustDialog,
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.18f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.Tune,
                                    contentDescription = "Atur Menit",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // GPS Scanning Indicator
            if (isGpsScanning) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Sedang memindai lokasi satelit GPS otomatis...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // GPS Status Message (if warning / error)
            if (gpsStatusMessage != null && !isGpsScanning) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = IslamicGold.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Info, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = gpsStatusMessage,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = onTriggerGpsScan) {
                                Text("Pindai Lagi", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Offline State Banner
            if (!isOnline) {
                item {
                    OfflineStateBanner(
                        message = stringResource(R.string.offline_state_banner)
                    )
                }
            }

            // Permission Denied State
            if (!hasLocationPermission) {
                item {
                    PermissionDeniedView(
                        title = stringResource(R.string.permission_denied_title),
                        description = stringResource(R.string.permission_denied_desc),
                        ctaLabel = stringResource(R.string.permission_denied_cta),
                        onCtaClick = onRequestLocationPermission
                    )
                }
            }

            // Loading State
            if (isLoading) {
                item {
                    LoadingStateView(
                        processText = stringResource(R.string.loading_state_title)
                    )
                }
            }

            // Error State
            if (errorMessage != null && !isLoading) {
                item {
                    ErrorStateView(
                        title = stringResource(R.string.error_state_title),
                        errorMessage = errorMessage,
                        retryLabel = stringResource(R.string.error_state_retry),
                        onRetryClick = onRetry
                    )
                }
            }

            // Empty State
            if (dailySchedule == null && !isLoading && errorMessage == null) {
                item {
                    EmptyStateView(
                        title = stringResource(R.string.empty_state_title),
                        description = stringResource(R.string.empty_state_desc),
                        actionLabel = stringResource(R.string.empty_state_cta),
                        onActionClick = onSelectCityClick
                    )
                }
            }

            // Main Content when Daily Schedule exists
            dailySchedule?.let { schedule ->
                // Hero Countdown Card
                item {
                    HeroCountdownCard(
                        schedule = schedule,
                        currentTimeMillis = currentTimeMillis
                    )
                }

                // Imsak & Fasting Reminder Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = IslamicGold.copy(alpha = 0.12f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Imsak & Batas Sahur (Kemenag RI)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Waktu Imsak: ${schedule.imsak.formattedTime} WIB • Batas ihtiyath 10 menit sebelum Subuh untuk mengakhiri sahur puasa Ramadhan & Sunnah.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }

                // Al-Qur'an Last Read Card
                quranProgress?.let { prog ->
                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                            LastReadCard(
                                progress = prog,
                                onClick = onOpenQuranClick
                            )
                        }
                    }
                }

                // Quick Access: Panduan Sholat & Wudhu Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clickable { onOpenPrayerGuide() },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Panduan Sholat, Wudhu & Doa",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Niat sholat 5 waktu, rukun bacaan, tata cara wudhu & dzikir",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                contentDescription = null,
                                tint = IslamicGold
                            )
                        }
                    }
                }

                // Section Header: Hari Ini & Batch Notification Controls
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.today_schedule),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = schedule.dateHijri,
                                style = MaterialTheme.typography.labelSmall,
                                color = IslamicGold,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Batch Notification Action Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { showExplanationDialog = true }
                                    .padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                                    contentDescription = "Panduan Mode Suara",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Arti Mode Suara",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Box {
                                OutlinedButton(
                                    onClick = { showBatchMenu = true },
                                    modifier = Modifier.height(32.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Tune,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Atur Semua", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                }

                                DropdownMenu(
                                    expanded = showBatchMenu,
                                    onDismissRequest = { showBatchMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Semua Adzan Penuh")
                                            }
                                        },
                                        onClick = {
                                            onBatchNotificationChange(NotificationMode.FULL_ADZAN)
                                            showBatchMenu = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Filled.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Semua Bunyi Singkat")
                                            }
                                        },
                                        onClick = {
                                            onBatchNotificationChange(NotificationMode.BEEP)
                                            showBatchMenu = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.AutoMirrored.Filled.VolumeOff, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Heningkan Semua (Mute)")
                                            }
                                        },
                                        onClick = {
                                            onBatchNotificationChange(NotificationMode.MUTE)
                                            showBatchMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Prayer Schedule Items
                items(schedule.toList()) { item ->
                    PrayerScheduleCard(
                        item = item,
                        notificationMode = notificationModes[item.name] ?: NotificationMode.FULL_ADZAN,
                        onSoundToggle = { onSoundToggle(item.name) },
                        onAdjustClick = { onOpenAdzanPlaylist(item.name) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationModeExplanationDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Fungsi Mode Suara Sholat",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mode 1: Adzan Penuh
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "1. Adzan Penuh (Speaker)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Memutar lantunan rekaman vokal adzan muadzin manusia secara lengkap dari awal hingga selesai saat waktu sholat tiba. Ideal untuk suasana rumah.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Mode 2: Bunyi Singkat
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = null,
                        tint = IslamicGold,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "2. Bunyi Singkat (Lonceng)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGold
                        )
                        Text(
                            text = "Membunyikan gema takbir 2x atau notifikasi santun tanpa kumandang adzan panjang. Sangat cocok saat Anda di kantor, kuliah, atau rapat.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Mode 3: Hening
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "3. Hening / Mute (Speaker Silang)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            text = "Mode senyap tanpa suara maupun getar. Hanya menampilkan notifikasi visual di status bar ponsel agar tidak mengganggu aktivitas Anda.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Saya Mengerti")
                }
            }
        }
    }
}

private data class CountdownTarget(
    val titleRes: Int,
    val formattedTime: String,
    val diffMillis: Long,
    val isTomorrow: Boolean
)

@Composable
private fun HeroCountdownCard(
    schedule: DailyPrayerSchedule,
    currentTimeMillis: Long
) {
    val items = schedule.toList()
    val upcomingToday = items.find { it.timeMillis > currentTimeMillis && it.name != PrayerName.SUNRISE }

    val target = if (upcomingToday != null) {
        CountdownTarget(
            titleRes = upcomingToday.name.titleRes,
            formattedTime = upcomingToday.formattedTime,
            diffMillis = (upcomingToday.timeMillis - currentTimeMillis).coerceAtLeast(0L),
            isTomorrow = false
        )
    } else {
        // All today's prayers have passed (after Isya night time). Target tomorrow's Imsak!
        val tomorrowImsakMillis = schedule.imsak.timeMillis + 24 * 60 * 60 * 1000L
        CountdownTarget(
            titleRes = PrayerName.IMSAK.titleRes,
            formattedTime = schedule.imsak.formattedTime,
            diffMillis = (tomorrowImsakMillis - currentTimeMillis).coerceAtLeast(0L),
            isTomorrow = true
        )
    }

    val hours = (target.diffMillis / 3600000)
    val minutes = (target.diffMillis % 3600000) / 60000
    val seconds = (target.diffMillis % 60000) / 1000
    val countdownStr = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    val labelText = if (target.isTomorrow) {
        "Menuju ${stringResource(id = target.titleRes)} (Besok)"
    } else {
        "Menuju ${stringResource(id = target.titleRes)}"
    }

    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val heroCardBg = if (isDark) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = heroCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = schedule.dateGregorian,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = labelText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = IslamicGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = countdownStr,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pukul ${target.formattedTime} WIB",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}
