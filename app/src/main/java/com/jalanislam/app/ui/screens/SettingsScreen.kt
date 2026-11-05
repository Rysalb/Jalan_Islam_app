package com.jalanislam.app.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jalanislam.app.R
import com.jalanislam.app.data.cities.IndonesianCities
import com.jalanislam.app.data.model.CityLocation
import com.jalanislam.app.data.model.PrayerAdjustment
import com.jalanislam.app.data.preferences.NotificationMode
import com.jalanislam.app.ui.components.DraftPreservationNotice
import com.jalanislam.app.ui.components.SuccessStateFeedback
import com.jalanislam.app.ui.components.ZeroSearchResultsView
import com.jalanislam.app.ui.theme.AppColorPalette
import com.jalanislam.app.ui.theme.AppThemeMode
import com.jalanislam.app.ui.theme.IslamicGold

@Composable
fun SettingsScreen(
    currentCityId: String,
    useGps: Boolean,
    currentLanguage: String,
    currentFontScale: Float,
    currentThemePalette: String = "emerald",
    currentThemeMode: String = "system",
    onSelectCity: (String) -> Unit,
    onToggleGps: (Boolean) -> Unit,
    onSelectLanguage: (String) -> Unit,
    onSelectFontScale: (Float) -> Unit,
    onSelectThemePalette: (String) -> Unit = {},
    onSelectThemeMode: (String) -> Unit = {},
    onOpenAdjustDialog: () -> Unit,
    onOpenAdzanPlaylist: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showCityDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showSuccessBanner by remember { mutableStateOf(false) }
    var testFeedbackMessage by remember { mutableStateOf<String?>(null) }

    val currentCity = IndonesianCities.findById(currentCityId) ?: IndonesianCities.getDefaultCity()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = null,
                        tint = IslamicGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.nav_settings),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Konfigurasi Jadwal, Bahasa & Notifikasi",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 8. Session / Draft Preservation Notice
            DraftPreservationNotice(
                message = stringResource(R.string.draft_preserved_notice),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 10. Success State Feedback
            if (showSuccessBanner) {
                SuccessStateFeedback(
                    title = stringResource(R.string.success_saved_title),
                    description = stringResource(R.string.success_saved_desc),
                    nextActionLabel = "Tutup",
                    onNextAction = { showSuccessBanner = false },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Section: Koreksi Menit Waktu Sholat
            SettingsSectionHeader(title = "Koreksi Waktu Sholat")
            SettingsCardItem(
                icon = Icons.Filled.Tune,
                title = stringResource(R.string.manual_adjustment_title),
                subtitle = "Sesuaikan menit (+/- 10 min) dengan masjid setempat",
                onClick = onOpenAdjustDialog
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section: Lokasi & Wilayah
            SettingsSectionHeader(title = "Lokasi & Koordinat")
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.MyLocation, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = stringResource(R.string.use_gps),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = if (useGps) "Otomatis dari koordinat GPS" else "Menggunakan kota pilihan",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                        Switch(
                            checked = useGps,
                            onCheckedChange = {
                                onToggleGps(it)
                                showSuccessBanner = true
                            }
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showCityDialog = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.LocationCity, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = stringResource(R.string.select_city),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${currentCity.name}, ${currentCity.province}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = IslamicGold,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Icon(Icons.Filled.ChevronRight, contentDescription = null)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section: Bahasa (Support Indonesia, Melayu, English, French, German)
            SettingsSectionHeader(title = stringResource(R.string.language_selection))
            SettingsCardItem(
                icon = Icons.Filled.Language,
                title = stringResource(R.string.language_selection),
                subtitle = when (currentLanguage) {
                    "ms" -> stringResource(R.string.lang_ms)
                    "en" -> stringResource(R.string.lang_en)
                    "fr" -> stringResource(R.string.lang_fr)
                    "de" -> stringResource(R.string.lang_de)
                    else -> stringResource(R.string.lang_id)
                },
                onClick = { showLanguageDialog = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section: Ukuran Huruf & Tampilan (Ramah Lansia)
            SettingsSectionHeader(title = "Ukuran Huruf (Aksesibilitas Lansia)")
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.FormatSize, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Besaran Teks Aplikasi",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Sangat direkomendasikan ukuran 'Besar' bagi orang tua agar teks Al-Qur'an dan waktu sholat terbaca jelas.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val scales = listOf(
                            0.85f to "Kecil",
                            1.0f to "Normal",
                            1.2f to "Besar (Lansia)",
                            1.4f to "Ekstra"
                        )
                        scales.forEach { (scale, label) ->
                            FilterChip(
                                selected = kotlin.math.abs(currentFontScale - scale) < 0.05f,
                                onClick = {
                                    onSelectFontScale(scale)
                                    showSuccessBanner = true
                                },
                                label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section: Tema & Warna Tampilan (Harmonisasi UI)
            SettingsSectionHeader(title = "Tema & Warna Tampilan")
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Palet Warna Nuansa Islami",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pilih nuansa warna aplikasi yang sinkron dan nyaman di mata Anda.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Palette options
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppColorPalette.entries.forEach { palette ->
                            val isSelected = currentThemePalette.equals(palette.id, ignoreCase = true)
                            Surface(
                                onClick = {
                                    onSelectThemePalette(palette.id)
                                    showSuccessBanner = true
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                ),
                                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(Color(palette.colorCode)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(15.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = palette.title,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        fontSize = 10.5.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Mode Tampilan (Sistem / Terang / Gelap)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Mode Tampilan Siang / Malam",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AppThemeMode.entries.forEach { mode ->
                            val isSelected = currentThemeMode.equals(mode.id, ignoreCase = true)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    onSelectThemeMode(mode.id)
                                    showSuccessBanner = true
                                },
                                label = {
                                    Text(
                                        text = mode.title,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                leadingIcon = if (isSelected) {
                                    { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                } else null
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section: Suara & Alarm Adzan
            SettingsSectionHeader(title = "Suara Adzan & Audio")
            SettingsCardItem(
                icon = Icons.Filled.MusicNote,
                title = "Playlist Adzan & Audio HP",
                subtitle = "Pilih suara Makkah, Madinah, Al-Aqsa, atau file custom dari HP",
                onClick = onOpenAdzanPlaylist
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section: Ketepatan Alarm & Baterai
            SettingsSectionHeader(title = "Pengoptimalan Alarm Adzan")
            SettingsCardItem(
                icon = Icons.Filled.BatteryAlert,
                title = "Pengaturan Hemat Baterai (Penting)",
                subtitle = "Nonaktifkan optimasi baterai agar suara adzan berbunyi tepat waktu",
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                        context.startActivity(intent)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section: Uji Coba Alarm & Notifikasi Masuk
            SettingsSectionHeader(title = "Uji Coba Alarm & Notifikasi")
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.NotificationsActive,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Tes Masuknya Notifikasi & Suara Adzan",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Gunakan fitur ini untuk memastikan notifikasi, nada adzan, dan getaran berfungsi sempurna di HP Anda baik saat aplikasi aktif maupun saat layar HP terkunci (sleep).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                com.jalanislam.app.alarm.PrayerAlarmScheduler.triggerTestNotification(context, delaySeconds = 0)
                                testFeedbackMessage = "Notifikasi & suara adzan uji coba langsung dibunyikan!"
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tes Sekarang", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                com.jalanislam.app.alarm.PrayerAlarmScheduler.triggerTestNotification(context, delaySeconds = 10)
                                testFeedbackMessage = "Alarm uji coba akan berdering dalam 10 detik. Silakan kunci layar HP Anda untuk menguji saat layar mati!"
                            },
                            modifier = Modifier.weight(1.1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Filled.Timer, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tes 10 Detik (Kunci HP)", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            com.jalanislam.app.alarm.PrayerAlarmScheduler.stopTestNotification(context)
                            testFeedbackMessage = "Suara adzan uji coba telah dihentikan."
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Filled.Stop, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Hentikan Suara Uji Coba", fontSize = 11.sp)
                    }

                    if (testFeedbackMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = testFeedbackMessage.orEmpty(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // City Selector Dialog
    if (showCityDialog) {
        CitySelectionDialog(
            currentCityId = currentCityId,
            onDismiss = { showCityDialog = false },
            onSelect = { cityId ->
                onSelectCity(cityId)
                showCityDialog = false
                showSuccessBanner = true
            }
        )
    }

    // Language Selector Dialog
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLang = currentLanguage,
            onDismiss = { showLanguageDialog = false },
            onSelect = { langCode ->
                onSelectLanguage(langCode)
                showLanguageDialog = false
                showSuccessBanner = true
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsCardItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null)
        }
    }
}

@Composable
private fun CitySelectionDialog(
    currentCityId: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val cities = remember(query) { IndonesianCities.search(query) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_city),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text(stringResource(R.string.search_city_hint)) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 6. Zero Search Results State
                if (cities.isEmpty() && query.isNotEmpty()) {
                    ZeroSearchResultsView(
                        query = query,
                        onResetQuery = { query = "" },
                        onFallbackAction = {
                            onSelect("jkt")
                        }
                    )
                }

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cities) { city ->
                        val isSelected = city.id == currentCityId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
                                .clickable { onSelect(city.id) }
                                .padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = city.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = city.province,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            if (isSelected) {
                                Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Tutup")
                }
            }
        }
    }
}

@Composable
private fun LanguageSelectionDialog(
    currentLang: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    val languages = listOf(
        "id" to stringResource(R.string.lang_id),
        "ms" to stringResource(R.string.lang_ms),
        "en" to stringResource(R.string.lang_en),
        "fr" to stringResource(R.string.lang_fr),
        "de" to stringResource(R.string.lang_de)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(R.string.language_selection),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                languages.forEach { (code, name) ->
                    val isSelected = code == currentLang
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
                            .clickable { onSelect(code) }
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        if (isSelected) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Tutup")
                }
            }
        }
    }
}
