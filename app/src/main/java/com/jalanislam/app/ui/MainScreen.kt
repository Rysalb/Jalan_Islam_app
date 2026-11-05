package com.jalanislam.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.jalanislam.app.R
import com.jalanislam.app.data.cities.IndonesianCities
import com.jalanislam.app.data.model.PrayerName
import com.jalanislam.app.data.quran.SurahInfo
import com.jalanislam.app.ui.components.AdzanPlaylistDialog
import com.jalanislam.app.ui.components.PrayerAdjustmentDialog
import com.jalanislam.app.ui.screens.HomeScreen
import com.jalanislam.app.ui.screens.MonthlyScheduleScreen
import com.jalanislam.app.ui.screens.QiblaScreen
import com.jalanislam.app.ui.screens.QuranScreen
import com.jalanislam.app.ui.screens.SettingsScreen
import com.jalanislam.app.ui.screens.SurahDetailScreen
import com.jalanislam.app.ui.theme.IslamicGold

enum class AppTab(val titleRes: Int, val icon: ImageVector) {
    HOME(R.string.nav_home, Icons.Filled.Home),
    QURAN(R.string.nav_quran, Icons.AutoMirrored.Filled.MenuBook),
    MONTHLY(R.string.nav_monthly, Icons.Filled.CalendarMonth),
    QIBLA(R.string.nav_qibla, Icons.Filled.Explore),
    SETTINGS(R.string.nav_settings, Icons.Filled.Settings)
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onRequestLocationPermission: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(AppTab.HOME) }
    var showAdjustDialog by remember { mutableStateOf(false) }
    var selectedPrayerForAdzanPlaylist by remember { mutableStateOf<PrayerName?>(null) }
    var selectedSurahForDetail by remember { mutableStateOf<SurahInfo?>(null) }

    val currentCity = remember(uiState.selectedCityId) {
        IndonesianCities.findById(uiState.selectedCityId) ?: IndonesianCities.getDefaultCity()
    }

    val activeLocationName = if (uiState.useGps && uiState.detectedLocation != null) {
        uiState.detectedLocation!!.cityName
    } else {
        currentCity.name
    }

    // Start/stop compass sensor depending on whether Qibla tab is active
    DisposableEffect(selectedTab) {
        if (selectedTab == AppTab.QIBLA) {
            viewModel.startCompass()
        }
        onDispose {
            if (selectedTab == AppTab.QIBLA) {
                viewModel.stopCompass()
            }
        }
    }

    // If user is viewing prayer guide
    if (uiState.isPrayerGuideOpen) {
        com.jalanislam.app.ui.screens.PrayerGuideScreen(
            onBackClick = { viewModel.closePrayerGuide() }
        )
        return
    }

    // If user is viewing a specific Surah detail screen
    selectedSurahForDetail?.let { surah ->
        SurahDetailScreen(
            surah = surah,
            quranRepo = viewModel.quranRepo,
            onBack = { selectedSurahForDetail = null }
        )
        return
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = NavigationBarDefaults.Elevation
            ) {
                AppTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = stringResource(id = tab.titleRes)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(id = tab.titleRes),
                                fontSize = 10.5.sp,
                                maxLines = 1,
                                softWrap = false,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            AppTab.HOME -> {
                HomeScreen(
                    dailySchedule = uiState.dailySchedule,
                    isLoading = uiState.isLoading,
                    errorMessage = uiState.errorMessage,
                    hasLocationPermission = uiState.hasLocationPermission,
                    isOnline = uiState.isOnline,
                    isGpsActive = uiState.useGps,
                    isGpsScanning = uiState.isGpsScanning,
                    gpsStatusMessage = uiState.gpsStatusMessage,
                    quranProgress = uiState.quranProgress,
                    notificationModes = uiState.notificationModes,
                    onSoundToggle = { viewModel.toggleNotificationSound(it) },
                    onBatchNotificationChange = { mode -> viewModel.setBatchNotificationMode(mode) },
                    onTriggerGpsScan = { viewModel.scanCurrentLocation() },
                    onOpenAdjustDialog = { showAdjustDialog = true },
                    onOpenAdzanPlaylist = { prayer -> selectedPrayerForAdzanPlaylist = prayer },
                    onOpenQuranClick = { selectedTab = AppTab.QURAN },
                    onOpenPrayerGuide = { viewModel.openPrayerGuide() },
                    onRequestLocationPermission = onRequestLocationPermission,
                    onRetry = { viewModel.recalculateSchedules() },
                    onSelectCityClick = { selectedTab = AppTab.SETTINGS },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            AppTab.QURAN -> {
                QuranScreen(
                    quranRepo = viewModel.quranRepo,
                    onSelectSurah = { surah -> selectedSurahForDetail = surah },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            AppTab.MONTHLY -> {
                MonthlyScheduleScreen(
                    monthlySchedules = uiState.monthlySchedules,
                    locationName = activeLocationName,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            AppTab.QIBLA -> {
                QiblaScreen(
                    qiblaInfo = uiState.qiblaInfo,
                    locationName = activeLocationName,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            AppTab.SETTINGS -> {
                SettingsScreen(
                    currentCityId = uiState.selectedCityId,
                    useGps = uiState.useGps,
                    currentLanguage = uiState.language,
                    currentFontScale = uiState.fontScale,
                    currentThemePalette = uiState.themePalette,
                    currentThemeMode = uiState.themeMode,
                    onSelectCity = { viewModel.selectCity(it) },
                    onToggleGps = { viewModel.toggleGps(it) },
                    onSelectLanguage = { viewModel.setLanguage(it) },
                    onSelectFontScale = { viewModel.setFontScale(it) },
                    onSelectThemePalette = { viewModel.setThemePalette(it) },
                    onSelectThemeMode = { viewModel.setThemeMode(it) },
                    onOpenAdjustDialog = { showAdjustDialog = true },
                    onOpenAdzanPlaylist = { selectedPrayerForAdzanPlaylist = PrayerName.FAJR },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        // Manual Minute Adjustment Dialog
        if (showAdjustDialog) {
            PrayerAdjustmentDialog(
                initialAdjustment = uiState.adjustments,
                onDismiss = { showAdjustDialog = false },
                onSave = { updated ->
                    viewModel.updateAdjustments(updated)
                    showAdjustDialog = false
                },
                onReset = {
                    viewModel.resetAdjustments()
                }
            )
        }

        // Adzan Audio Playlist & Custom Device File Picker Dialog
        selectedPrayerForAdzanPlaylist?.let { prayer ->
            AdzanPlaylistDialog(
                targetPrayer = prayer,
                currentSoundId = viewModel.getSelectedAdzanSound(prayer),
                onDismiss = { selectedPrayerForAdzanPlaylist = null },
                onSelectSound = { soundItem ->
                    viewModel.saveAdzanSound(prayer, soundItem.id)
                    selectedPrayerForAdzanPlaylist = null
                }
            )
        }
    }
}
