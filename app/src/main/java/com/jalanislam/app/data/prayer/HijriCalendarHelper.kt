package com.jalanislam.app.data.prayer

import java.util.Calendar
import kotlin.math.floor

object HijriCalendarHelper {

    private val HIJRI_MONTHS = arrayOf(
        "Muharram", "Safar", "Rabi'ul Awwal", "Rabi'ul Akhir",
        "Jumadil Ula", "Jumadil Akhir", "Rajab", "Sya'ban",
        "Ramadhan", "Syawwal", "Dzulqa'dah", "Dzulhijjah"
    )

    /**
     * Converts Gregorian Calendar date to formatted Hijri Date string (e.g., "15 Ramadhan 1448 H").
     */
    fun convertToHijri(calendar: Calendar): String {
        val y = calendar.get(Calendar.YEAR)
        val m = calendar.get(Calendar.MONTH) + 1
        val d = calendar.get(Calendar.DAY_OF_MONTH)

        // Julian Day
        var year = y
        var month = m
        if (month <= 2) {
            year -= 1
            month += 12
        }
        val a = floor(year / 100.0)
        val b = 2 - a + floor(a / 4.0)
        val jd = floor(365.25 * (year + 4716)) + floor(30.6001 * (month + 1)) + d + b - 1524.5

        // Islamic calendar calculation based on Tabular Islamic Calendar
        val epoch = 1948439.5
        val daysSinceEpoch = jd - epoch
        val cycle = floor(daysSinceEpoch / 10631.0)
        val remainingDays = daysSinceEpoch - (cycle * 10631.0)
        val yearInCycle = floor((remainingDays - 0.5) / 354.36667)
        val hijriYear = (cycle * 30 + yearInCycle + 1).toInt()

        val dayOfYear = remainingDays - floor(yearInCycle * 354.36667)
        var hijriMonth = floor((dayOfYear - 0.5) / 29.5).toInt()
        if (hijriMonth < 0) hijriMonth = 0
        if (hijriMonth > 11) hijriMonth = 11

        var hijriDay = (dayOfYear - floor(hijriMonth * 29.5) + 1).toInt()
        if (hijriDay < 1) hijriDay = 1
        if (hijriDay > 30) hijriDay = 30

        return "$hijriDay ${HIJRI_MONTHS[hijriMonth]} $hijriYear H"
    }
}

