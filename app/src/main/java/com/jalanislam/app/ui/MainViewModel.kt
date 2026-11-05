package com.jalanislam.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jalanislam.app.alarm.PrayerAlarmScheduler
import com.jalanislam.app.data.cities.IndonesianCities
import com.jalanislam.app.data.model.DailyPrayerSchedule
import com.jalanislam.app.data.model.PrayerAdjustment
import com.jalanislam.app.data.model.PrayerName
import com.jalanislam.app.data.model.QiblaInfo
import com.jalanislam.app.data.network.NetworkMonitor
import com.jalanislam.app.data.prayer.AstronomicalPrayerCalculator
import com.jalanislam.app.data.preferences.AppPreferences
import com.jalanislam.app.data.preferences.NotificationMode
import com.jalanislam.app.data.quran.QuranReadingProgress
import com.jalanislam.app.data.quran.QuranRepository
import com.jalanislam.app.data.sensor.CompassSensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

import com.jalanislam.app.data.location.AppLocationManager
import com.jalanislam.app.data.location.DetectedLocation

data class MainUiState(
    val dailySchedule: DailyPrayerSchedule? = null,
    val monthlySchedules: List<DailyPrayerSchedule> = emptyList(),
    val qiblaInfo: QiblaInfo = QiblaInfo(295.15f, 0f, 295.15f, 7916.0, 3),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val hasLocationPermission: Boolean = true,
    val isOnline: Boolean = true,
    val quranProgress: QuranReadingProgress? = null,
    val adjustments: PrayerAdjustment = PrayerAdjustment(),
    val selectedCityId: String = "jkt",
    val useGps: Boolean = false,
    val detectedLocation: DetectedLocation? = null,
    val isGpsScanning: Boolean = false,
    val gpsStatusMessage: String? = null,
    val isPrayerGuideOpen: Boolean = false,
    val language: String = "id",
    val fontScale: Float = 1.0f,
    val themePalette: String = "emerald",
    val themeMode: String = "system",
    val notificationModes: Map<PrayerName, NotificationMode> = emptyMap(),
    val showOnboarding: Boolean = false,
    val showFeatureTour: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val quranRepo = QuranRepository(application)
    private val prefs = AppPreferences(application)
    private val compassManager = CompassSensorManager(application)
    private val networkMonitor = NetworkMonitor(application)
    private val locationManager = AppLocationManager(application)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
        observeCompass()
        observeNetwork()
        observeQuranProgress()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { online ->
                _uiState.value = _uiState.value.copy(isOnline = online)
            }
        }
    }

    private fun observeQuranProgress() {
        viewModelScope.launch {
            quranRepo.progressFlow.collect { prog ->
                _uiState.value = _uiState.value.copy(quranProgress = prog)
            }
        }
    }

    fun getSelectedAdzanSound(prayer: PrayerName): String {
        return prefs.getSelectedAdzanSound(prayer)
    }

    fun saveAdzanSound(prayer: PrayerName, soundId: String) {
        prefs.setSelectedAdzanSound(prayer, soundId)
        PrayerAlarmScheduler.scheduleNextAlarm(getApplication())
    }

    fun completeOnboarding(defaultMode: NotificationMode, startTour: Boolean) {
        prefs.setAllNotificationModes(defaultMode)
        prefs.setCompletedOnboarding(true)
        val notifMap = PrayerName.entries.associateWith { defaultMode }
        _uiState.value = _uiState.value.copy(
            showOnboarding = false,
            notificationModes = notifMap,
            showFeatureTour = startTour
        )
        PrayerAlarmScheduler.scheduleNextAlarm(getApplication())
    }

    fun startFeatureTour() {
        _uiState.value = _uiState.value.copy(showFeatureTour = true)
    }

    fun finishFeatureTour() {
        prefs.setCompletedFeatureTour(true)
        _uiState.value = _uiState.value.copy(showFeatureTour = false)
    }

    private fun loadInitialData() {
        val adjustments = prefs.loadAdjustments()
        val cityId = prefs.selectedCityIdFlow.value
        val useGps = prefs.useGpsFlow.value
        val lang = prefs.languageFlow.value
        val fontScale = prefs.fontScaleFlow.value
        val palette = prefs.themePaletteFlow.value
        val mode = prefs.themeModeFlow.value
        val isFirstLaunch = !prefs.hasCompletedOnboarding()

        val notifModes = PrayerName.entries.associateWith { prefs.getNotificationMode(it) }

        _uiState.value = _uiState.value.copy(
            adjustments = adjustments,
            selectedCityId = cityId,
            useGps = useGps,
            language = lang,
            fontScale = fontScale,
            themePalette = palette,
            themeMode = mode,
            notificationModes = notifModes,
            showOnboarding = isFirstLaunch
        )

        recalculateSchedules()
    }

    private fun observeCompass() {
        viewModelScope.launch {
            compassManager.qiblaInfoFlow.collect { info ->
                _uiState.value = _uiState.value.copy(qiblaInfo = info)
            }
        }
    }

    fun startCompass() {
        val state = _uiState.value
        val (lat, lng) = if (state.useGps && state.detectedLocation != null) {
            Pair(state.detectedLocation.latitude, state.detectedLocation.longitude)
        } else {
            val city = IndonesianCities.findById(state.selectedCityId) ?: IndonesianCities.getDefaultCity()
            Pair(city.latitude, city.longitude)
        }
        compassManager.updateCoordinates(lat, lng)
        compassManager.startListening()
    }

    fun stopCompass() {
        compassManager.stopListening()
    }

    fun recalculateSchedules() {
        val state = _uiState.value
        val (lat, lng, locName) = if (state.useGps && state.detectedLocation != null) {
            Triple(state.detectedLocation.latitude, state.detectedLocation.longitude, state.detectedLocation.cityName)
        } else {
            val city = IndonesianCities.findById(state.selectedCityId) ?: IndonesianCities.getDefaultCity()
            Triple(city.latitude, city.longitude, city.name)
        }

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        try {
            val nowCal = Calendar.getInstance()
            val daily = AstronomicalPrayerCalculator.calculateDaily(
                nowCal, lat, lng, locName, state.adjustments
            )
            val monthly = AstronomicalPrayerCalculator.calculate30Days(
                nowCal, lat, lng, locName, state.adjustments
            )

            compassManager.updateCoordinates(lat, lng)

            _uiState.value = _uiState.value.copy(
                dailySchedule = daily,
                monthlySchedules = monthly,
                isLoading = false,
                errorMessage = null
            )

            // Reschedule alarm
            PrayerAlarmScheduler.scheduleNextAlarm(getApplication())
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Gagal menghitung jadwal: ${e.localizedMessage}"
            )
        }
    }

    fun scanCurrentLocation() {
        if (!locationManager.hasLocationPermission()) {
            _uiState.value = _uiState.value.copy(
                hasLocationPermission = false,
                gpsStatusMessage = "Izin lokasi diperlukan untuk pemindaian GPS otomatis."
            )
            return
        }

        _uiState.value = _uiState.value.copy(isGpsScanning = true, gpsStatusMessage = "Memindai koordinat GPS satelit...")
        locationManager.scanCurrentLocation(
            onSuccess = { loc ->
                prefs.setUseGps(true)
                prefs.saveGpsLocation(loc.latitude, loc.longitude, loc.cityName)
                val nearest = com.jalanislam.app.data.cities.IndonesianCities.findNearest(loc.latitude, loc.longitude)
                prefs.setSelectedCityId(nearest.id)

                _uiState.value = _uiState.value.copy(
                    isGpsScanning = false,
                    useGps = true,
                    detectedLocation = loc,
                    selectedCityId = nearest.id,
                    gpsStatusMessage = null
                )
                recalculateSchedules()
            },
            onError = { err ->
                _uiState.value = _uiState.value.copy(
                    isGpsScanning = false,
                    gpsStatusMessage = err
                )
                recalculateSchedules()
            }
        )
    }

    fun updateAdjustments(newAdjustments: PrayerAdjustment) {
        prefs.saveAdjustments(newAdjustments)
        _uiState.value = _uiState.value.copy(adjustments = newAdjustments)
        recalculateSchedules()
    }

    fun resetAdjustments() {
        prefs.resetAdjustments()
        _uiState.value = _uiState.value.copy(adjustments = PrayerAdjustment())
        recalculateSchedules()
    }

    fun selectCity(cityId: String) {
        prefs.setSelectedCityId(cityId)
        prefs.setUseGps(false)
        _uiState.value = _uiState.value.copy(
            selectedCityId = cityId,
            useGps = false,
            detectedLocation = null,
            gpsStatusMessage = null
        )
        recalculateSchedules()
    }

    fun toggleGps(useGps: Boolean) {
        prefs.setUseGps(useGps)
        _uiState.value = _uiState.value.copy(useGps = useGps)
        if (useGps) {
            scanCurrentLocation()
        } else {
            _uiState.value = _uiState.value.copy(detectedLocation = null, gpsStatusMessage = null)
            recalculateSchedules()
        }
    }

    fun setLanguage(langCode: String) {
        prefs.setLanguage(langCode)
        _uiState.value = _uiState.value.copy(language = langCode)
    }

    fun setFontScale(scale: Float) {
        prefs.setFontScale(scale)
        _uiState.value = _uiState.value.copy(fontScale = scale)
    }

    fun setLocationPermissionGranted(granted: Boolean) {
        _uiState.value = _uiState.value.copy(hasLocationPermission = granted)
        if (granted && _uiState.value.useGps) {
            scanCurrentLocation()
        }
    }

    fun toggleNotificationSound(prayer: PrayerName) {
        val current = _uiState.value.notificationModes[prayer] ?: NotificationMode.FULL_ADZAN
        val next = when (current) {
            NotificationMode.FULL_ADZAN -> NotificationMode.BEEP
            NotificationMode.BEEP -> NotificationMode.MUTE
            NotificationMode.MUTE -> NotificationMode.FULL_ADZAN
        }
        prefs.setNotificationMode(prayer, next)
        val updatedMap = _uiState.value.notificationModes.toMutableMap().apply { put(prayer, next) }
        _uiState.value = _uiState.value.copy(notificationModes = updatedMap)
    }

    fun setBatchNotificationMode(mode: NotificationMode) {
        prefs.setAllNotificationModes(mode)
        val updatedMap = PrayerName.entries.associateWith { mode }
        _uiState.value = _uiState.value.copy(notificationModes = updatedMap)
    }

    fun openPrayerGuide() {
        _uiState.value = _uiState.value.copy(isPrayerGuideOpen = true)
    }

    fun closePrayerGuide() {
        _uiState.value = _uiState.value.copy(isPrayerGuideOpen = false)
    }

    fun setThemePalette(paletteId: String) {
        prefs.setThemePalette(paletteId)
        _uiState.value = _uiState.value.copy(themePalette = paletteId)
    }

    fun setThemeMode(mode: String) {
        prefs.setThemeMode(mode)
        _uiState.value = _uiState.value.copy(themeMode = mode)
    }
}

