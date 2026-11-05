package com.jalanislam.app.data.quran

data class SurahInfo(
    val number: Int,
    val nameArabic: String,
    val nameLatin: String,
    val translation: String,
    val totalVerses: Int,
    val revelationType: String, // "Makkiyyah" or "Madaniyyah"
    val startJuz: Int
)

data class AyahItem(
    val surahNumber: Int,
    val ayahNumber: Int,
    val textArabic: String,
    val textLatin: String,
    val textTranslation: String,
    val audioUrl: String,
    val isMemorized: Boolean = false,
    val isRevealed: Boolean = true // For Tahfidz mode (Hide / Reveal)
)

data class QuranReadingProgress(
    val lastReadSurahNumber: Int = 1,
    val lastReadSurahName: String = "Al-Fatihah",
    val lastReadAyahNumber: Int = 1,
    val lastReadTimestamp: Long = System.currentTimeMillis(),
    val targetKhatamDays: Int = 30,
    val memorizedAyahKeys: Set<String> = emptySet() // "surah:ayah"
) {
    val totalMemorized: Int get() = memorizedAyahKeys.size

    // Estimated progress calculation (out of 604 pages or total verses)
    val dailyVerseTarget: Int
        get() = (6236.0 / targetKhatamDays.coerceAtLeast(1)).toInt().coerceAtLeast(1)
}

