package com.jalanislam.app.data.model

data class PrayerTimeItem(
    val name: PrayerName,
    val timeMillis: Long,
    val formattedTime: String,
    val isNext: Boolean = false,
    val isPassed: Boolean = false,
    val offsetMinutes: Int = 0
)

data class DailyPrayerSchedule(
    val dateGregorian: String,
    val dateHijri: String,
    val locationName: String,
    val imsak: PrayerTimeItem,
    val fajr: PrayerTimeItem,
    val sunrise: PrayerTimeItem,
    val dhuhr: PrayerTimeItem,
    val asr: PrayerTimeItem,
    val maghrib: PrayerTimeItem,
    val isha: PrayerTimeItem
) {
    fun toList(): List<PrayerTimeItem> = listOf(
        imsak, fajr, sunrise, dhuhr, asr, maghrib, isha
    )
}

