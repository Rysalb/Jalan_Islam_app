package com.jalanislam.app.data.model

import androidx.annotation.StringRes
import com.jalanislam.app.R

enum class PrayerName(
    @StringRes val titleRes: Int,
    val isObligatory: Boolean
) {
    IMSAK(R.string.prayer_imsak, false),
    FAJR(R.string.prayer_fajr, true),
    SUNRISE(R.string.prayer_sunrise, false),
    DHUHR(R.string.prayer_dhuhr, true),
    ASR(R.string.prayer_asr, true),
    MAGHRIB(R.string.prayer_maghrib, true),
    ISHA(R.string.prayer_isha, true);

    companion object {
        fun fromString(name: String?): PrayerName {
            return entries.find { it.name.equals(name, ignoreCase = true) } ?: FAJR
        }
    }
}

