package com.jalanislam.app.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jalanislam.app.R
import com.jalanislam.app.data.adzan.AdzanSoundManager
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
    onRequestLocationPermission: () -> Unit,
    onRequestAppPermissions: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(AppTab.HOME) }
    var showAdjustDialog by remember { mutableStateOf(false) }
    var selectedPrayerForAdzanPlaylist by remember { mutableStateOf<PrayerName?>(null) }
    var selectedSurahForDetail by remember { mutableStateOf<SurahInfo?>(null) }

    val context = LocalContext.current
    val soundManager = remember { AdzanSoundManager(context) }
    var customAudioList by remember { mutableStateOf(soundManager.listCustomAdzans()) }
    var filePickerSuccessMessage by remember { mutableStateOf<String?>(null) }
    var filePickerErrorMessage by remember { mutableStateOf<String?>(null) }

    // SAF File Picker registered safely at root composable (before RESUMED state conflicts)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            val fileName = soundManager.getFileNameFromUri(uri)
            val savedItem = soundManager.saveCustomAudio(uri, fileName)
            if (savedItem != null) {
                customAudioList = soundManager.listCustomAdzans()
                filePickerSuccessMessage = "Audio HP '${savedItem.title}' berhasil ditambahkan!"
                filePickerErrorMessage = null
                selectedPrayerForAdzanPlaylist?.let { prayer ->
                    viewModel.saveAdzanSound(prayer, savedItem.id)
                }
            } else {
                filePickerErrorMessage = "Gagal membaca berkas audio yang dipilih."
                filePickerSuccessMessage = null
            }
        }
    }

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
            onBack = { selectedSurahForDetail = null },
            currentLanguage = uiState.language
        )
        return
    }

    Scaffold(
        bottomBar = {
            FloatingGlassNavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
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
                    onRestartFeatureTour = { viewModel.startFeatureTour() },
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
                initialPrayer = prayer,
                getSoundForPrayer = { p -> viewModel.getSelectedAdzanSound(p) },
                onDismiss = {
                    selectedPrayerForAdzanPlaylist = null
                    filePickerSuccessMessage = null
                    filePickerErrorMessage = null
                },
                onSelectSound = { p, soundItem ->
                    viewModel.saveAdzanSound(p, soundItem.id)
                },
                onApplyToAll = { soundItem ->
                    PrayerName.entries.filter { it.isObligatory }.forEach { p ->
                        viewModel.saveAdzanSound(p, soundItem.id)
                    }
                },
                onPickCustomAudio = {
                    filePickerLauncher.launch("audio/*")
                },
                customList = customAudioList,
                pickerSuccessMessage = filePickerSuccessMessage,
                pickerErrorMessage = filePickerErrorMessage,
                onDeleteCustomAudio = { item ->
                    soundManager.deleteCustomAudio(item)
                    customAudioList = soundManager.listCustomAdzans()
                }
            )
        }

        // First-Time Onboarding & Audio Setup Dialog (Prevents User Being Shocked)
        if (uiState.showOnboarding) {
            com.jalanislam.app.ui.components.OnboardingDialog(
                onComplete = { defaultMode, startTour ->
                    viewModel.completeOnboarding(defaultMode, startTour)
                },
                onRequestPermissions = onRequestAppPermissions
            )
        }

        // In-App Interactive Feature Tour (Walkthrough of each screen & button)
        if (uiState.showFeatureTour) {
            com.jalanislam.app.ui.components.InteractiveFeatureTourDialog(
                onDismiss = { viewModel.finishFeatureTour() }
            )
        }
    }
}

@Composable
private fun FloatingGlassNavBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        tonalElevation = 6.dp,
        shadowElevation = 10.dp,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTab.entries.forEach { tab ->
                val isSelected = selectedTab == tab
                val animScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.05f else 1.0f,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "tab_scale"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
                            else Color.Transparent
                        )
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 8.dp, horizontal = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.graphicsLayer {
                            scaleX = animScale
                            scaleY = animScale
                        }
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = stringResource(id = tab.titleRes),
                            tint = if (isSelected) MaterialTheme.colorScheme.primary
                                   else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = stringResource(id = tab.titleRes),
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                                   else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
