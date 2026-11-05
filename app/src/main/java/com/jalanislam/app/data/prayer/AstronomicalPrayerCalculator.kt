package com.jalanislam.app.data.prayer

import com.jalanislam.app.data.model.DailyPrayerSchedule
import com.jalanislam.app.data.model.PrayerAdjustment
import com.jalanislam.app.data.model.PrayerName
import com.jalanislam.app.data.model.PrayerTimeItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.*

/**
 * Astronomical Prayer Time Calculation Engine.
 * Implements Solar Coordinates algorithm (Jean Meeus) and conforms to
 * Kementerian Agama Republik Indonesia (Kemenag RI) standards:
 * - Subuh: 20 degrees
 * - Isya: 18 degrees
 * - Ashar: Shafi'i (shadow multiplier 1)
 * - Ihtiyat: +2 minutes
 * - Imsak: 10 minutes before Subuh
 */
object AstronomicalPrayerCalculator {

    // Coordinates of Ka'bah in Makkah
    const val KABAH_LATITUDE = 21.422487
    const val KABAH_LONGITUDE = 39.826206
    private const val EARTH_RADIUS_KM = 6371.0

    // Kemenag standard parameters
    private const val FAJR_ANGLE = 20.0
    private const val ISHA_ANGLE = 18.0
    private const val DEFAULT_IHTIYAT_MINUTES = 2
    private const val IMSAK_MARGIN_MINUTES = 10

    /**
     * Calculates daily prayer schedule for a specific date and coordinates.
     */
    fun calculateDaily(
        calendar: Calendar,
        latitude: Double,
        longitude: Double,
        locationName: String,
        adjustments: PrayerAdjustment = PrayerAdjustment(),
        timeZone: TimeZone = calendar.timeZone
    ): DailyPrayerSchedule {
        val cal = calendar.clone() as Calendar
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val timezoneOffsetHours = timeZone.getOffset(cal.timeInMillis) / 3600000.0

        // Calculate astronomical times (in hours 0.0 - 24.0)
        val times = computePrayerHours(year, month, day, latitude, longitude, timezoneOffsetHours)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val gregorianFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())

        val nowMillis = System.currentTimeMillis()

        fun createItem(name: PrayerName, baseHour: Double, manualOffset: Int): PrayerTimeItem {
            val itemCal = cal.clone() as Calendar
            val totalMinutes = (baseHour * 60).roundToInt() + manualOffset
            val hour = (totalMinutes / 60) % 24
            val minute = totalMinutes % 60
            itemCal.set(Calendar.HOUR_OF_DAY, hour)
            itemCal.set(Calendar.MINUTE, minute)
            itemCal.set(Calendar.SECOND, 0)
            itemCal.set(Calendar.MILLISECOND, 0)

            val millis = itemCal.timeInMillis
            return PrayerTimeItem(
                name = name,
                timeMillis = millis,
                formattedTime = timeFormat.format(Date(millis)),
                isPassed = millis < nowMillis,
                offsetMinutes = manualOffset
            )
        }

        val fajrItem = createItem(PrayerName.FAJR, times.fajr, adjustments.fajrOffset)
        val sunriseItem = createItem(PrayerName.SUNRISE, times.sunrise, adjustments.sunriseOffset)
        val dhuhrItem = createItem(PrayerName.DHUHR, times.dhuhr, adjustments.dhuhrOffset)
        val asrItem = createItem(PrayerName.ASR, times.asr, adjustments.asrOffset)
        val maghribItem = createItem(PrayerName.MAGHRIB, times.maghrib, adjustments.maghribOffset)
        val ishaItem = createItem(PrayerName.ISHA, times.isha, adjustments.ishaOffset)

        // Imsak is 10 minutes prior to Subuh (Fajr)
        val imsakBaseHour = times.fajr - (IMSAK_MARGIN_MINUTES / 60.0)
        val imsakItem = createItem(PrayerName.IMSAK, imsakBaseHour, adjustments.imsakOffset)

        // Determine next upcoming prayer
        val items = listOf(imsakItem, fajrItem, sunriseItem, dhuhrItem, asrItem, maghribItem, ishaItem)
        var nextFound = false
        val markedItems = items.map { item ->
            if (!nextFound && item.timeMillis > nowMillis) {
                nextFound = true
                item.copy(isNext = true)
            } else {
                item
            }
        }

        val hijriDateStr = HijriCalendarHelper.convertToHijri(cal)

        return DailyPrayerSchedule(
            dateGregorian = gregorianFormat.format(cal.time),
            dateHijri = hijriDateStr,
            locationName = locationName,
            imsak = markedItems[0],
            fajr = markedItems[1],
            sunrise = markedItems[2],
            dhuhr = markedItems[3],
            asr = markedItems[4],
            maghrib = markedItems[5],
            isha = markedItems[6]
        )
    }

    /**
     * Calculates prayer schedules for 30 consecutive days from startDate.
     */
    fun calculate30Days(
        startCalendar: Calendar,
        latitude: Double,
        longitude: Double,
        locationName: String,
        adjustments: PrayerAdjustment = PrayerAdjustment()
    ): List<DailyPrayerSchedule> {
        val list = mutableListOf<DailyPrayerSchedule>()
        val current = startCalendar.clone() as Calendar

        for (i in 0 until 30) {
            list.add(calculateDaily(current, latitude, longitude, locationName, adjustments))
            current.add(Calendar.DAY_OF_YEAR, 1)
        }
        return list
    }

    /**
     * Calculates Qibla direction (bearing) in degrees from True North (0..360).
     */
    fun calculateQiblaBearing(latitude: Double, longitude: Double): Float {
        val userLatRad = Math.toRadians(latitude)
        val kabahLatRad = Math.toRadians(KABAH_LATITUDE)
        val deltaLngRad = Math.toRadians(KABAH_LONGITUDE - longitude)

        val y = sin(deltaLngRad)
        val x = cos(userLatRad) * tan(kabahLatRad) - sin(userLatRad) * cos(deltaLngRad)

        var qiblaRad = atan2(y, x)
        var qiblaDeg = Math.toDegrees(qiblaRad)
        if (qiblaDeg < 0) {
            qiblaDeg += 360.0
        }
        return qiblaDeg.toFloat()
    }

    /**
     * Calculates great-circle distance to Ka'bah in Kilometers using Haversine formula.
     */
    fun calculateDistanceToKabahKm(latitude: Double, longitude: Double): Double {
        val lat1Rad = Math.toRadians(latitude)
        val lat2Rad = Math.toRadians(KABAH_LATITUDE)
        val dLat = Math.toRadians(KABAH_LATITUDE - latitude)
        val dLng = Math.toRadians(KABAH_LONGITUDE - longitude)

        val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLng / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS_KM * c
    }

    // --- Core Astronomical Formulas ---

    private data class RawPrayerTimes(
        val fajr: Double,
        val sunrise: Double,
        val dhuhr: Double,
        val asr: Double,
        val maghrib: Double,
        val isha: Double
    )

    private fun computePrayerHours(
        year: Int,
        month: Int,
        day: Int,
        latitude: Double,
        longitude: Double,
        timezone: Double
    ): RawPrayerTimes {
        // Julian Day at 0h UT
        val jd = julianDay(year, month, day) - (longitude / (15.0 * 24.0))

        // Solar coordinates
        val d = jd - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(Math.toRadians(g)) + 0.020 * sin(Math.toRadians(2 * g)))

        val e = 23.439 - 0.00000036 * d
        val ra = fixAngle(Math.toDegrees(atan2(cos(Math.toRadians(e)) * sin(Math.toRadians(l)), cos(Math.toRadians(l))))) / 15.0

        val declination = Math.toDegrees(asin(sin(Math.toRadians(e)) * sin(Math.toRadians(l))))
        val equationOfTime = (q / 15.0) - fixHour(ra)

        // Mid-Day (Solar Noon)
        val midDay = fixHour(12.0 + timezone - (longitude / 15.0) - equationOfTime)

        // Dhuhr (+ ihtiyat)
        val dhuhr = midDay + (DEFAULT_IHTIYAT_MINUTES / 60.0)

        // Sunrise & Sunset angle (0.833 degree standard atmospheric refraction)
        val sunAlt = 0.833
        val sunriseHourAngle = hourAngle(latitude, declination, -sunAlt)
        val sunrise = midDay - sunriseHourAngle
        val sunset = midDay + sunriseHourAngle

        // Fajr (Subuh angle: 20 degrees below horizon)
        val fajrHourAngle = hourAngle(latitude, declination, -FAJR_ANGLE)
        val fajr = midDay - fajrHourAngle + (DEFAULT_IHTIYAT_MINUTES / 60.0)

        // Isha (Isya angle: 18 degrees below horizon)
        val ishaHourAngle = hourAngle(latitude, declination, -ISHA_ANGLE)
        val isha = midDay + ishaHourAngle + (DEFAULT_IHTIYAT_MINUTES / 60.0)

        // Asr (Shafi'i shadow ratio = 1, sun altitude is above horizon / positive)
        val asrAlt = Math.toDegrees(atan(1.0 / (1.0 + tan(Math.toRadians(abs(latitude - declination))))))
        val asrHourAngle = hourAngle(latitude, declination, asrAlt)
        val asr = midDay + asrHourAngle + (DEFAULT_IHTIYAT_MINUTES / 60.0)

        // Maghrib (+ ihtiyat)
        val maghrib = sunset + (DEFAULT_IHTIYAT_MINUTES / 60.0)

        return RawPrayerTimes(
            fajr = fajr,
            sunrise = sunrise,
            dhuhr = dhuhr,
            asr = asr,
            maghrib = maghrib,
            isha = isha
        )
    }

    private fun julianDay(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun hourAngle(latitude: Double, declination: Double, angle: Double): Double {
        val latRad = Math.toRadians(latitude)
        val decRad = Math.toRadians(declination)
        val angRad = Math.toRadians(angle)
        val cosHA = (sin(angRad) - sin(latRad) * sin(decRad)) / (cos(latRad) * cos(decRad))
        val clampedCos = cosHA.coerceIn(-1.0, 1.0)
        return Math.toDegrees(acos(clampedCos)) / 15.0
    }

    private fun fixAngle(angle: Double): Double {
        var a = angle - 360.0 * floor(angle / 360.0)
        if (a < 0) a += 360.0
        return a
    }

    private fun fixHour(hour: Double): Double {
        var h = hour - 24.0 * floor(hour / 24.0)
        if (h < 0) h += 24.0
        return h
    }
}

