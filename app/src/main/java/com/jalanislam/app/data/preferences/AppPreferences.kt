package com.jalanislam.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.jalanislam.app.data.model.PrayerAdjustment
import com.jalanislam.app.data.model.PrayerName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class NotificationMode {
    FULL_ADZAN,
    BEEP,
    MUTE
}

class AppPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _adjustmentsFlow = MutableStateFlow(loadAdjustments())
    val adjustmentsFlow: StateFlow<PrayerAdjustment> = _adjustmentsFlow.asStateFlow()

    private val _useGpsFlow = MutableStateFlow(prefs.getBoolean(KEY_USE_GPS, false))
    val useGpsFlow: StateFlow<Boolean> = _useGpsFlow.asStateFlow()

    private val _selectedCityIdFlow = MutableStateFlow(prefs.getString(KEY_CITY_ID, "jkt") ?: "jkt")
    val selectedCityIdFlow: StateFlow<String> = _selectedCityIdFlow.asStateFlow()

    private val _languageFlow = MutableStateFlow(prefs.getString(KEY_LANG, "id") ?: "id")
    val languageFlow: StateFlow<String> = _languageFlow.asStateFlow()

    private val _fontScaleFlow = MutableStateFlow(prefs.getFloat(KEY_FONT_SCALE, 1.0f))
    val fontScaleFlow: StateFlow<Float> = _fontScaleFlow.asStateFlow()

    private val _themePaletteFlow = MutableStateFlow(prefs.getString(KEY_THEME_PALETTE, "emerald") ?: "emerald")
    val themePaletteFlow: StateFlow<String> = _themePaletteFlow.asStateFlow()

    private val _themeModeFlow = MutableStateFlow(prefs.getString(KEY_THEME_MODE, "system") ?: "system")
    val themeModeFlow: StateFlow<String> = _themeModeFlow.asStateFlow()

    fun loadAdjustments(): PrayerAdjustment {
        return PrayerAdjustment(
            imsakOffset = prefs.getInt(KEY_OFFSET_IMSAK, 0),
            fajrOffset = prefs.getInt(KEY_OFFSET_FAJR, 0),
            sunriseOffset = prefs.getInt(KEY_OFFSET_SUNRISE, 0),
            dhuhrOffset = prefs.getInt(KEY_OFFSET_DHUHR, 0),
            asrOffset = prefs.getInt(KEY_OFFSET_ASR, 0),
            maghribOffset = prefs.getInt(KEY_OFFSET_MAGHRIB, 0),
            ishaOffset = prefs.getInt(KEY_OFFSET_ISHA, 0)
        )
    }

    fun saveAdjustments(adjustment: PrayerAdjustment) {
        prefs.edit()
            .putInt(KEY_OFFSET_IMSAK, adjustment.imsakOffset)
            .putInt(KEY_OFFSET_FAJR, adjustment.fajrOffset)
            .putInt(KEY_OFFSET_SUNRISE, adjustment.sunriseOffset)
            .putInt(KEY_OFFSET_DHUHR, adjustment.dhuhrOffset)
            .putInt(KEY_OFFSET_ASR, adjustment.asrOffset)
            .putInt(KEY_OFFSET_MAGHRIB, adjustment.maghribOffset)
            .putInt(KEY_OFFSET_ISHA, adjustment.ishaOffset)
            .apply()
        _adjustmentsFlow.value = adjustment
    }

    fun setOffsetForPrayer(prayer: PrayerName, offsetMinutes: Int) {
        val current = _adjustmentsFlow.value
        val updated = current.copyWithOffset(prayer, offsetMinutes)
        saveAdjustments(updated)
    }

    fun resetAdjustments() {
        saveAdjustments(PrayerAdjustment())
    }

    fun setUseGps(useGps: Boolean) {
        prefs.edit().putBoolean(KEY_USE_GPS, useGps).apply()
        _useGpsFlow.value = useGps
    }

    fun setSelectedCityId(cityId: String) {
        prefs.edit().putString(KEY_CITY_ID, cityId).apply()
        _selectedCityIdFlow.value = cityId
    }

    fun setLanguage(langCode: String) {
        prefs.edit().putString(KEY_LANG, langCode).apply()
        _languageFlow.value = langCode
    }

    fun setFontScale(scale: Float) {
        val clamped = scale.coerceIn(0.85f, 1.4f)
        prefs.edit().putFloat(KEY_FONT_SCALE, clamped).apply()
        _fontScaleFlow.value = clamped
    }

    fun setThemePalette(paletteId: String) {
        prefs.edit().putString(KEY_THEME_PALETTE, paletteId).apply()
        _themePaletteFlow.value = paletteId
    }

    fun setThemeMode(mode: String) {
        prefs.edit().putString(KEY_THEME_MODE, mode).apply()
        _themeModeFlow.value = mode
    }

    fun getNotificationMode(prayer: PrayerName): NotificationMode {
        val modeStr = prefs.getString("notif_${prayer.name}", NotificationMode.FULL_ADZAN.name)
        return try {
            NotificationMode.valueOf(modeStr ?: NotificationMode.FULL_ADZAN.name)
        } catch (e: Exception) {
            NotificationMode.FULL_ADZAN
        }
    }

    fun setNotificationMode(prayer: PrayerName, mode: NotificationMode) {
        prefs.edit().putString("notif_${prayer.name}", mode.name).apply()
    }

    fun setAllNotificationModes(mode: NotificationMode) {
        val editor = prefs.edit()
        PrayerName.entries.forEach { prayer ->
            editor.putString("notif_${prayer.name}", mode.name)
        }
        editor.apply()
    }

    fun getSelectedAdzanSound(prayer: PrayerName): String {
        return if (prayer == PrayerName.FAJR) {
            prefs.getString("adzan_sound_${prayer.name}", "adzan_subuh") ?: "adzan_subuh"
        } else {
            prefs.getString("adzan_sound_${prayer.name}", "adzan_islamcan") ?: "adzan_islamcan"
        }
    }

    fun setSelectedAdzanSound(prayer: PrayerName, soundId: String) {
        prefs.edit().putString("adzan_sound_${prayer.name}", soundId).apply()
    }

    fun hasCompletedOnboarding(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    fun setCompletedOnboarding(completed: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }

    fun hasCompletedFeatureTour(): Boolean {
        return prefs.getBoolean(KEY_FEATURE_TOUR_COMPLETED, false)
    }

    fun setCompletedFeatureTour(completed: Boolean) {
        prefs.edit().putBoolean(KEY_FEATURE_TOUR_COMPLETED, completed).apply()
    }

    fun saveGpsLocation(lat: Double, lng: Double, cityName: String) {
        prefs.edit()
            .putFloat(KEY_GPS_LAT, lat.toFloat())
            .putFloat(KEY_GPS_LNG, lng.toFloat())
            .putString(KEY_GPS_CITY, cityName)
            .apply()
    }

    fun getGpsLocation(): Triple<Double, Double, String>? {
        if (!prefs.contains(KEY_GPS_LAT) || !prefs.contains(KEY_GPS_LNG)) return null
        val lat = prefs.getFloat(KEY_GPS_LAT, 0f).toDouble()
        val lng = prefs.getFloat(KEY_GPS_LNG, 0f).toDouble()
        val city = prefs.getString(KEY_GPS_CITY, "") ?: ""
        return Triple(lat, lng, city)
    }

    companion object {
        private const val PREFS_NAME = "jalan_islam_prefs"
        private const val KEY_USE_GPS = "pref_use_gps"
        private const val KEY_CITY_ID = "pref_city_id"
        private const val KEY_LANG = "pref_language"
        private const val KEY_FONT_SCALE = "pref_font_scale"
        private const val KEY_THEME_PALETTE = "pref_theme_palette"
        private const val KEY_THEME_MODE = "pref_theme_mode"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_FEATURE_TOUR_COMPLETED = "feature_tour_completed"
        private const val KEY_GPS_LAT = "gps_latitude"
        private const val KEY_GPS_LNG = "gps_longitude"
        private const val KEY_GPS_CITY = "gps_city_name"

        private const val KEY_OFFSET_IMSAK = "offset_imsak"
        private const val KEY_OFFSET_FAJR = "offset_fajr"
        private const val KEY_OFFSET_SUNRISE = "offset_sunrise"
        private const val KEY_OFFSET_DHUHR = "offset_dhuhr"
        private const val KEY_OFFSET_ASR = "offset_asr"
        private const val KEY_OFFSET_MAGHRIB = "offset_maghrib"
        private const val KEY_OFFSET_ISHA = "offset_isha"
    }
}

