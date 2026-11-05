package com.jalanislam.app.data.model

data class PrayerAdjustment(
    val imsakOffset: Int = 0,
    val fajrOffset: Int = 0,
    val sunriseOffset: Int = 0,
    val dhuhrOffset: Int = 0,
    val asrOffset: Int = 0,
    val maghribOffset: Int = 0,
    val ishaOffset: Int = 0
) {
    fun getOffsetFor(prayer: PrayerName): Int = when (prayer) {
        PrayerName.IMSAK -> imsakOffset
        PrayerName.FAJR -> fajrOffset
        PrayerName.SUNRISE -> sunriseOffset
        PrayerName.DHUHR -> dhuhrOffset
        PrayerName.ASR -> asrOffset
        PrayerName.MAGHRIB -> maghribOffset
        PrayerName.ISHA -> ishaOffset
    }

    fun copyWithOffset(prayer: PrayerName, offset: Int): PrayerAdjustment = when (prayer) {
        PrayerName.IMSAK -> copy(imsakOffset = offset.coerceIn(-10, 10))
        PrayerName.FAJR -> copy(fajrOffset = offset.coerceIn(-10, 10))
        PrayerName.SUNRISE -> copy(sunriseOffset = offset.coerceIn(-10, 10))
        PrayerName.DHUHR -> copy(dhuhrOffset = offset.coerceIn(-10, 10))
        PrayerName.ASR -> copy(asrOffset = offset.coerceIn(-10, 10))
        PrayerName.MAGHRIB -> copy(maghribOffset = offset.coerceIn(-10, 10))
        PrayerName.ISHA -> copy(ishaOffset = offset.coerceIn(-10, 10))
    }
}

data class QiblaInfo(
    val qiblaBearing: Float,       // Ka'bah angle from True North (0-360)
    val deviceHeading: Float,      // Phone's current heading from True North (0-360)
    val qiblaRelativeAngle: Float, // Relative angle to point compass needle
    val distanceToKabahKm: Double, // Distance in Kilometers
    val sensorAccuracy: Int        // SensorManager.SENSOR_STATUS_*
)

data class CityLocation(
    val id: String,
    val name: String,
    val province: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneId: String
)

