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
    val isRead: Boolean = false,
    val isRevealed: Boolean = true // For Tahfidz mode (Hide / Reveal)
)

data class QuranReadingProgress(
    val lastReadSurahNumber: Int = 1,
    val lastReadSurahName: String = "Al-Fatihah",
    val lastReadAyahNumber: Int = 1,
    val lastReadTimestamp: Long = System.currentTimeMillis(),
    val targetKhatamDays: Int = 30,
    val memorizedAyahKeys: Set<String> = emptySet(), // "surah:ayah"
    val readAyahKeys: Set<String> = emptySet(), // "surah:ayah"
    val todayReadCount: Int = 0,
    val todayDateString: String = ""
) {
    val totalMemorized: Int get() = memorizedAyahKeys.size
    val totalRead: Int get() = readAyahKeys.size
    val khatamPercentage: Float get() = (readAyahKeys.size.toFloat() / 6236f * 100f).coerceIn(0f, 100f)

    // Daily verse target according to user's khatam commitment (e.g. 30 days = ~208 verses/day)
    val dailyVerseTarget: Int
        get() = (6236.0 / targetKhatamDays.coerceIn(1, 365)).toInt().coerceAtLeast(1)
}
